package com.neoflex.statement.controller;

import com.neoflex.statement.dto.LoanOfferDto;
import com.neoflex.statement.dto.LoanStatementRequestDto;
import com.neoflex.statement.service.PostService;
import com.neoflex.statement.validation.PreScoringValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class StatementController {

    private final PreScoringValidationService preScoringValidationServiceImpl;
    private final PostService postServiceImpl;

    @Autowired
    public StatementController(PreScoringValidationService preScoringValidationServiceImpl, PostService postServiceImpl) {
        this.preScoringValidationServiceImpl = preScoringValidationServiceImpl;
        this.postServiceImpl = postServiceImpl;
    }

    @Operation(
            summary = "Прескоринг и запрос на расчёт возможных условий кредита",
            description = "На основании LoanStatementRequestDto происходит прескоринг," +
                    " создаётся 4 кредитных предложения LoanOfferDto"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "409", description = "incorrect parameter")})
    @PostMapping("/statement")
    public List<LoanOfferDto> preScoring(
            @RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        log.info("запрошенный loanStatementRequestDto: {}", loanStatementRequestDto);
        List<LoanOfferDto> result;
        preScoringValidationServiceImpl.resultsOfValidation(loanStatementRequestDto);
        result = postServiceImpl.postDealStatement(loanStatementRequestDto);
        log.info("StatementController ответ на пост запрос: {}", result);
        return result;
    }

    @Operation(
            summary = "Выбор одного из предложений",
            description = "По API приходит LoanOfferDto потом отправляется POST-запрос на" +
                    " /deal/offer/select в МС deal через RestClient"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "409", description = "incorrect parameter")})
    @PostMapping("/statement/offer")
    public void choiceOfOffer(
            @RequestBody LoanOfferDto loanOfferDto) {
        log.info("запрошенный loanOfferDto: {}", loanOfferDto);
        postServiceImpl.postDealOfferSelect(loanOfferDto);
    }

}
