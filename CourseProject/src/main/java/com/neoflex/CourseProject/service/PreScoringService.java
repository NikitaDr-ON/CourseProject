package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.LoanOfferDto;

import java.math.BigDecimal;
import java.util.List;

public interface PreScoringService {
    List<LoanOfferDto> resultsOfPreScoring(BigDecimal requestedAmount);
}
