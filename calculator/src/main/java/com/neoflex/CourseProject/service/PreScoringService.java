package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.LoanOfferDto;
import com.neoflex.CourseProject.dto.LoanStatementRequestDto;

import java.util.List;

public interface PreScoringService {
    List<LoanOfferDto> resultsOfPreScoring(LoanStatementRequestDto loanStatementRequestDto);
}
