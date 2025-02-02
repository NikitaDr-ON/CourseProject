package com.neoflex.CourseProject.validation;

import com.neoflex.CourseProject.dto.LoanStatementRequestDto;

public interface PreScoringValidationService {
    void resultsOfValidation(LoanStatementRequestDto loanStatementRequestDto);
}
