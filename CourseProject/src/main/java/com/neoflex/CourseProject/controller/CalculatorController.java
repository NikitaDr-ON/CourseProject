package com.neoflex.CourseProject.controller;

import com.neoflex.CourseProject.dto.CreditDto;
import com.neoflex.CourseProject.dto.LoanOfferDto;
import com.neoflex.CourseProject.dto.LoanStatementRequestDto;
import com.neoflex.CourseProject.dto.ScoringDataDto;
import com.neoflex.CourseProject.exception.ValidationException;
import com.neoflex.CourseProject.service.PreScoringService;
import com.neoflex.CourseProject.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CalculatorController {
    @Autowired
    PreScoringService preScoringService;
    @Autowired
    ValidationService validationService;

    @PostMapping("/calculator/offers")
    public List<LoanOfferDto> calculationOfPossibleLoanTerms(@RequestBody LoanStatementRequestDto loanStatementRequestDto) throws ValidationException {
        validationService.resultsOfValidation(loanStatementRequestDto);
        return (preScoringService.resultsOfPreScoring(loanStatementRequestDto.getAmount()));
    }


    @PostMapping("/calculator/calc")
    public CreditDto dataScoring (@RequestBody ScoringDataDto scoringDataDto){
        return null;
    }

}
