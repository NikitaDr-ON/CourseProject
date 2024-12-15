package com.neoflex.deal.controller;

import com.neoflex.deal.dto.FinishRegistrationRequestDto;
import com.neoflex.deal.dto.LoanOfferDto;
import com.neoflex.deal.dto.LoanStatementRequestDto;
import com.neoflex.deal.service.FinishRegistrationService;
import com.neoflex.deal.service.PossibleConditionsService;
import com.neoflex.deal.service.SelectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DealController {

    private final PossibleConditionsService possibleConditionsService;
    private final FinishRegistrationService finishRegistrationService;
    private final SelectService selectService;

    @Autowired
    public DealController(PossibleConditionsService possibleConditionsService,
                          FinishRegistrationService finishRegistrationService, SelectService selectService) {
        this.possibleConditionsService = possibleConditionsService;
        this.finishRegistrationService = finishRegistrationService;
        this.selectService = selectService;
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
    @PostMapping("/deal/statement")
    public List<LoanOfferDto> calculatingOfPossibleLoanTerms(
            @RequestBody LoanStatementRequestDto loanStatementRequestDto) {
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
            @ApiResponse(responseCode = "404", description = "not found")})
    @PostMapping("/deal/offer/select")
    public void selectOffer(@RequestBody LoanOfferDto loanOfferDto) {
        selectService.selectStatement(loanOfferDto);
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
            @ApiResponse(responseCode = "404", description = "not found")})
    @PostMapping("/deal/calculate/{statementId}")
    public void completeRegistration(@RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto,
                                     @PathVariable String statementId) {
        finishRegistrationService.finishRegistration(finishRegistrationRequestDto, statementId);
    }


}
