package com.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CreditDto {

    @Schema(description = "сумма кредита", example = "20000")
    private BigDecimal amount;
    @Schema(description = "срок кредита", example = "6")
    private Integer term;
    @Schema(description = "ежемесячная выплата", example = "3333.33")
    private BigDecimal monthlyPayment;
    @Schema(description = "рейтинг", example = "21")
    private BigDecimal rate;
    @Schema(description = "итоговая сумма кредита", example = "21214.47")
    private BigDecimal psk;
    @Schema(description = "включена ли страховка", example = "true")
    private Boolean isInsuranceEnabled;
    @Schema(description = "является ли зарплатным клиентом", example = "true")
    private Boolean isSalaryClient;
    private List<PaymentScheduleElementDto> paymentSchedule;

}
