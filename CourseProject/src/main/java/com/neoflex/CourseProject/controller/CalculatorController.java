package com.neoflex.CourseProject.controller;


import com.neoflex.CourseProject.dto.CreditDto;
import com.neoflex.CourseProject.dto.LoanOfferDto;
import com.neoflex.CourseProject.dto.LoanStatementRequestDto;
import com.neoflex.CourseProject.dto.ScoringDataDto;
import com.neoflex.CourseProject.service.PreScoringService;
import com.neoflex.CourseProject.service.ScoringService;
import com.neoflex.CourseProject.service.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "Calculator API")
public class CalculatorController {

    private final ScoringService scoringServiceImpl;
    private final PreScoringService preScoringServiceImpl;
    private final ValidationService validationServiceImpl;

    @Autowired
    public CalculatorController(PreScoringService preScoringServiceImpl, ScoringService scoringServiceImpl,
                                ValidationService validationServiceImpl) {
        this.preScoringServiceImpl = preScoringServiceImpl;
        this.scoringServiceImpl = scoringServiceImpl;
        this.validationServiceImpl = validationServiceImpl;
    }

    @Operation(
            summary = "расчёт возможных условий кредита",
            description = "На основании LoanStatementRequestDto происходит прескоринг, создаётся 4 кредитных предложения LoanOfferDto"
    )
    @PostMapping("/calculator/offers")
    public List<LoanOfferDto> calculationOfPossibleLoanTerms(@RequestBody LoanStatementRequestDto loanStatementRequestDto){
        log.info("requested loanStatementRequestDto: {}", loanStatementRequestDto);
        validationServiceImpl.resultsOfValidation(loanStatementRequestDto);
        log.info("response list of LoanOfferDto: {}", preScoringServiceImpl.resultsOfPreScoring(loanStatementRequestDto.getAmount()));
        return (preScoringServiceImpl.resultsOfPreScoring(loanStatementRequestDto.getAmount()));
    }

    @Operation(
            summary = "валидация присланных данных + скоринг данных + полный расчет параметров кредита",
            description = "Происходит скоринг данных, высчитывание итоговой ставки(rate),\n" +
                    " полной стоимости кредита(psk), размер ежемесячного платежа(monthlyPayment), график ежемесячных платежей "
    )
    @PostMapping("/calculator/calc")
    public CreditDto dataScoring (@RequestBody ScoringDataDto scoringDataDto){
        log.info("requested scoringDataDto: {}", scoringDataDto);
        log.info("creditDto response: {}", scoringServiceImpl.scoring(scoringDataDto));
        return scoringServiceImpl.scoring(scoringDataDto);
    }

}
