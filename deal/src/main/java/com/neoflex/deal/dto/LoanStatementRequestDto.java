package com.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class LoanStatementRequestDto {

    @Schema(description = "запрошенная сумма кредита", example = "20000")
    private BigDecimal amount;
    @Schema(description = "срок кредита", example = "6")
    private Integer term;
    @Schema(description = "имя", example = "firstName")
    private String firstName;
    @Schema(description = "фамилия", example = "lastName")
    private String lastName;
    @Schema(description = "отчество", example = "middleName")
    private String middleName;
    @Schema(description = "адрес электронной почты", example = "email@mail.com")
    private String email;
    @Schema(description = "дата рождения", format = "yyyy-mm-dd", example = "2000-02-23")
    private LocalDate birthdate;
    @Schema(description = "серия паспорта", example = "1234")
    private String passportSeries;
    @Schema(description = "номер паспорта", example = "123456")
    private String passportNumber;

}
