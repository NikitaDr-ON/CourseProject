package com.neoflex.deal.service;

import com.neoflex.deal.dto.CreditDto;
import com.neoflex.deal.dto.LoanOfferDto;
import com.neoflex.deal.dto.LoanStatementRequestDto;
import com.neoflex.deal.dto.ScoringDataDto;

import java.util.List;

public interface PostService {
    List<LoanOfferDto> postOffers(LoanStatementRequestDto loanStatementRequestDto);
    CreditDto postLoanOfferDto(ScoringDataDto scoringDataDto);
}
