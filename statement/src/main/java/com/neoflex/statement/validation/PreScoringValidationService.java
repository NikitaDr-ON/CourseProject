package com.neoflex.statement.validation;


import com.neoflex.statement.dto.LoanStatementRequestDto;

public interface PreScoringValidationService {
    void resultsOfValidation(LoanStatementRequestDto loanStatementRequestDto);
}
