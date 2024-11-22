package com.neoflex.CourseProject.controller;

import com.neoflex.CourseProject.dto.CreditDto;
import com.neoflex.CourseProject.dto.LoanOfferDto;
import com.neoflex.CourseProject.dto.LoanStatementRequestDto;
import com.neoflex.CourseProject.dto.ScoringDataDto;
import com.neoflex.CourseProject.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Calculator API")
public class CalculatorController {

    private final PreScoring implPreScoringService;
    private final Scoring implScoringService;
    private final Validation implValidationService;

    @Autowired
    public CalculatorController(PreScoring implPreScoringService, Scoring implScoringService, Validation implValidationService) {
        this.implPreScoringService = implPreScoringService;
        this.implScoringService = implScoringService;
        this.implValidationService = implValidationService;
    }

    @Operation(
            summary = "расчёт возможных условий кредита",
            description = "На основании LoanStatementRequestDto происходит прескоринг, создаётся 4 кредитных предложения LoanOfferDto"
    )
    @PostMapping("/calculator/offers")
    public List<LoanOfferDto> calculationOfPossibleLoanTerms(@RequestBody LoanStatementRequestDto loanStatementRequestDto){
        implValidationService.resultsOfValidation(loanStatementRequestDto);
        return (implPreScoringService.resultsOfPreScoring(loanStatementRequestDto.getAmount()));
    }

    @Operation(
            summary = "валидация присланных данных + скоринг данных + полный расчет параметров кредита",
            description = "Происходит скоринг данных, высчитывание итоговой ставки(rate),\n" +
                    " полной стоимости кредита(psk), размер ежемесячного платежа(monthlyPayment), график ежемесячных платежей "
    )
    @PostMapping("/calculator/calc")
    public CreditDto dataScoring (@RequestBody ScoringDataDto scoringDataDto){
        return implScoringService.scoring(scoringDataDto);
    }

}
