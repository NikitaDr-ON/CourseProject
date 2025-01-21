package com.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class PaymentScheduleElementDto {

    @Schema(description = "номер выплаты", example = "0")
    private Integer number;
    @Schema(description = "дата выплаты", format = "yyyy-mm-dd", example = "2024-11-27")
    private LocalDate date;
    @Schema(description = "сумма ежемесячной выплаты кредита вместе с процентами", example = "3677.59")
    private BigDecimal totalPayment;
    @Schema(description = "базовый платеж", example = "33333.33")
    private BigDecimal interestPayment;
    @Schema(description = "платеж по процентам", example = "344.26")
    private BigDecimal debtPayment;
    @Schema(description = "остаток кредита", example = "0")
    private BigDecimal remainingDebt;

}
