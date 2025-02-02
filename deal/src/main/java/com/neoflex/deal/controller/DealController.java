package com.neoflex.deal.controller;

import com.neoflex.deal.dto.FinishRegistrationRequestDto;
import com.neoflex.deal.dto.LoanOfferDto;
import com.neoflex.deal.dto.LoanStatementRequestDto;
import com.neoflex.deal.enums.Theme;
import com.neoflex.deal.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/deal")
public class DealController {

    private final PossibleConditionsService possibleConditionsService;
    private final FinishRegistrationService finishRegistrationService;
    private final SelectService selectService;
    private final MessageSenderService messageSenderService;
    private final FillEmailMessageDtoService fillEmailMessageDtoService;

    @Autowired
    public DealController(PossibleConditionsService possibleConditionsService,
                          FinishRegistrationService finishRegistrationService, SelectService selectService,
                          MessageSenderService messageSenderService,
                          FillEmailMessageDtoService fillEmailMessageDtoService) {
        this.possibleConditionsService = possibleConditionsService;
        this.finishRegistrationService = finishRegistrationService;
        this.selectService = selectService;
        this.messageSenderService = messageSenderService;
        this.fillEmailMessageDtoService = fillEmailMessageDtoService;
    }

    @Operation(
            summary = "расчёт возможных условий кредита",
            description =
                    "Создается Client на основе LoanStatementRequestDto, создается Statement с связью на client," +
                            " отправляется post запрос на микросервис calculator, в ответ приходит лист LoanOfferDto," +
                            "каждому элементу списка присваивается id statement "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "409", description = "incorrect parameter")})
    @PostMapping("/statement")
    public List<LoanOfferDto> calculatePossibleLoanTerms(
            @RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        log.info("DealController calculatePossibleLoanTerms входящий LoanStatementRequestDto: {}",
                loanStatementRequestDto);
        return possibleConditionsService.getPossibleConditions(loanStatementRequestDto);
    }

    @Operation(
            summary = "выбор одного из предложений",
            description = "По id из LoanOfferDto достается из бд Statement," +
                    " в заявке обновляется статус, лист StatementStatusHistoryDto," +
                    "предложение LoanOfferDto устанавливается в поле appliedOffer, потом Statement сохраняется "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "not found"),
            @ApiResponse(responseCode = "409", description = "incorrect parameter")})
    @PostMapping("/offer/select")
    public void selectOffer(@RequestBody LoanOfferDto loanOfferDto) {
        log.info("DealController selectOffer входящий LoanOfferDto: {}", loanOfferDto);
        selectService.selectStatement(loanOfferDto);
        messageSenderService.sendRequests(fillEmailMessageDtoService.fillEmailMessageDto(loanOfferDto.getStatementId(),
                Theme.FINISH_REGISTRATION));
    }

    @Operation(
            summary = "Завершение регистрации и полный подсчёт кредита.",
            description = "по API приходит объект FinishRegistrationRequestDto и параметр statementId (String)," +
                    " Достаётся из БД заявка(Statement) по statementId," +
                    " ScoringDataDto насыщается информацией из FinishRegistrationRequestDto и Client," +
                    " который хранится в Statement. " +
                    " Отправляется POST запрос на /calculator/calc МС Калькулятор" +
                    " с телом ScoringDataDto через RestClient." +
                    " На основе полученного из кредитного конвейера CreditDto создаётся сущность Credit" +
                    " и сохраняется в базу со статусом CALCULATED." +
                    " В заявке обновляется статус, история статусов. " +
                    " Заявка сохраняется"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "not found"),
            @ApiResponse(responseCode = "409", description = "incorrect parameter")})
    @PostMapping("/deal/calculate/{statementId}")
    public void completeRegistration(@RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto,
                                     @PathVariable String statementId) {
        log.info("DealController completeRegistration входящий FinishRegistrationRequestDto: {}, statementId: {}",
                finishRegistrationRequestDto, statementId);
        finishRegistrationService.finishRegistration(finishRegistrationRequestDto, statementId);
        messageSenderService.sendRequests(fillEmailMessageDtoService.fillEmailMessageDto(UUID.fromString(statementId),
                Theme.CREATE_DOCUMENTS));
    }

    @Operation(
            summary = "Запрос на отправку документов.",
            description = "отправляется запрос на отправку документов через kafka, которое должен обработать mc dossier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "not found")})
    @PostMapping("/deal/calculate/{statementId}/send")
    public void sendRequestForSending(@PathVariable String statementId) {
        log.info("DealController sendRequestForSending входящий statementId: {}", statementId);
        messageSenderService.sendRequests(fillEmailMessageDtoService.fillEmailMessageDto(UUID.fromString(statementId),
                Theme.SEND_DOCUMENTS));
    }

    @Operation(
            summary = "Запрос на подписание документов.",
            description = "отправляется запрос на подписание документов через kafka," +
                    " которое должен обработать mc dossier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "not found")})
    @PostMapping("/deal/calculate/{statementId}/sign")
    public void sendRequestForSign(@PathVariable String statementId) {
        log.info("DealController sendRequestForSign входящий statementId: {}", statementId);
        messageSenderService.sendRequests(fillEmailMessageDtoService.fillEmailMessageDto(UUID.fromString(statementId),
                Theme.SEND_SES));
    }

    @Operation(
            summary = "Подписание документов.",
            description = "подписание документов"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "not found")})
    @PostMapping("/deal/calculate/{statementId}/code")
    public void signingDocuments(@PathVariable String statementId) {
        log.info("DealController signingDocuments входящий statementId: {}", statementId);
        messageSenderService.sendRequests(fillEmailMessageDtoService.fillEmailMessageDto(UUID.fromString(statementId),
                Theme.CREDIT_ISSUED));
    }

}
