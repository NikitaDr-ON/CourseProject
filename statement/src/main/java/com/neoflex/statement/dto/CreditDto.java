package com.neoflex.statement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CreditDto {
    @Schema(name = "amount", example = "20000")
    private BigDecimal amount;
    @Schema(name = "term", example = "6")
    private Integer term;
    @Schema(name = "monthlyPayment", example = "3333.33")
    private BigDecimal monthlyPayment;
    @Schema(name = "rate", example = "21")
    private BigDecimal rate;
    @Schema(name = "psk", example = "21214.47")
    private BigDecimal psk;
    @Schema(name = "isInsuranceEnabled", example = "true")
    private Boolean isInsuranceEnabled;
    @Schema(name = "isSalaryClient", example = "true")
    private Boolean isSalaryClient;
    private List<PaymentScheduleElementDto> paymentSchedule;

}
