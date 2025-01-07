package com.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class LoanOfferDto {

    @Schema(description = "id заявки", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID statementId;
    @Schema(description = "запрошенная сумма кредита", example = "20000")
    private BigDecimal requestedAmount;
    @Schema(name = "totalAmount", description = "сумма ежемесячной выплаты кредита вместе с процентами", example = "30000")
    private BigDecimal totalAmount;
    @Schema(description = "срок кредита", example = "6")
    private Integer term;
    @Schema(description = "ежемесячная выплата", example = "3333.33")
    private BigDecimal monthlyPayment;
    @Schema(description = "рейтинг", example = "21")
    private BigDecimal rate;
    @Schema(description = "включена ли страховка", example = "true")
    private Boolean isInsuranceEnabled;
    @Schema(description = "является ли зарплатным клиентом", example = "true")
    private Boolean isSalaryClient;

}
