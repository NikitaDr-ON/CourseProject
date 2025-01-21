package com.neoflex.statement.service;

import com.neoflex.statement.dto.LoanOfferDto;
import com.neoflex.statement.dto.LoanStatementRequestDto;

import java.util.List;

public interface PostService {
    List<LoanOfferDto> postDealStatement(LoanStatementRequestDto loanStatementRequestDto);
    void postDealOfferSelect(LoanOfferDto loanOfferDto);
}
