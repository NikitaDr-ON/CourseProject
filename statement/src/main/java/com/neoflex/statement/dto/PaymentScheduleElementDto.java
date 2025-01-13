package com.neoflex.statement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class PaymentScheduleElementDto {

    @Schema(name = "number", example = "0")
    private Integer number;
    @Schema(name = "date", format = "yyyy-mm-dd", example = "2024-11-27")
    private LocalDate date;
    @Schema(name = "totalPayment", example = "3677.59")
    private BigDecimal totalPayment;
    @Schema(name = "interestPayment", example = "33333.33")
    private BigDecimal interestPayment;
    @Schema(name = "debtPayment", example = "344.26")
    private BigDecimal debtPayment;
    @Schema(name = "remainingDebt", example = "0")
    private BigDecimal remainingDebt;

}
