package com.neoflex.deal.service;

import com.neoflex.deal.dto.LoanOfferDto;
import com.neoflex.deal.dto.LoanStatementRequestDto;

import java.util.List;

public interface PossibleConditionsService {
    List<LoanOfferDto> getPossibleConditions(LoanStatementRequestDto loanStatementRequestDto);
}
