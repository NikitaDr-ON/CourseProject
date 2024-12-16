package com.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class LoanOfferDto {

    @Schema(name = "id заявки", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID statementId;
    @Schema(name = "запрошенная сумма кредита", example = "20000")
    private BigDecimal requestedAmount;
    @Schema(name = "сумма ежемесячной выплаты кредита вместе с процентами", example = "30000")
    private BigDecimal totalAmount;
    @Schema(name = "срок кредита", example = "6")
    private Integer term;
    @Schema(name = "ежемесячная выплата", example = "3333.33")
    private BigDecimal monthlyPayment;
    @Schema(name = "рейтинг", example = "21")
    private BigDecimal rate;
    @Schema(name = "включена ли страховка", example = "true")
    private Boolean isInsuranceEnabled;
    @Schema(name = "является ли зарплатным клиентом", example = "true")
    private Boolean isSalaryClient;

}
