package com.neoflex.deal.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppliedOffer {

    private LoanOfferDto appliedLoanOfferDto;

}
