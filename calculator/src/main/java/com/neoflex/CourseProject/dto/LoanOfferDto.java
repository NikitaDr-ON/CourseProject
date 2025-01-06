package com.neoflex.CourseProject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class LoanOfferDto {

    @Schema(name = "statementId", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID statementId;
    @Schema(name = "requestedAmount", example = "20000")
    private BigDecimal requestedAmount;
    @Schema(name = "totalAmount", example = "30000")
    private BigDecimal totalAmount;
    @Schema(name = "term", example = "6")
    private Integer term;
    @Schema(name = "monthlyPayment", example = "3333.33")
    private BigDecimal monthlyPayment;
    @Schema(name = "rate", example = "21")
    private BigDecimal rate;
    @Schema(name = "isInsuranceEnabled", example = "true")
    private Boolean isInsuranceEnabled;
    @Schema(name = "isSalaryClient", example = "true")
    private Boolean isSalaryClient;

}
