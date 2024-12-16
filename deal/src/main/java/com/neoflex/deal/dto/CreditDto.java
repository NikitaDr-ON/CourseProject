package com.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CreditDto {

    @Schema(name = "сумма кредита", example = "20000")
    private BigDecimal amount;
    @Schema(name = "срок кредита", example = "6")
    private Integer term;
    @Schema(name = "ежемесячная выплата", example = "3333.33")
    private BigDecimal monthlyPayment;
    @Schema(name = "рейтинг", example = "21")
    private BigDecimal rate;
    @Schema(name = "итоговая сумма кредита", example = "21214.47")
    private BigDecimal psk;
    @Schema(name = "включена ли страховка", example = "true")
    private Boolean isInsuranceEnabled;
    @Schema(name = "является ли зарплатным клиентом", example = "true")
    private Boolean isSalaryClient;
    private List<PaymentScheduleElementDto> paymentSchedule;

}
