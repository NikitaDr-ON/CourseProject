package com.neoflex.CourseProject.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class LoanOfferDto {

    private UUID statementId;
    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private final Boolean isInsuranceEnabled;
    private final Boolean isSalaryClient;

}
