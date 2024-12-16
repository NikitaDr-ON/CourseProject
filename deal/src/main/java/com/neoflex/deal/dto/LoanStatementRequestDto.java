package com.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class LoanStatementRequestDto {

    @Schema(name = "запрошенная сумма кредита", example = "20000")
    private BigDecimal amount;
    @Schema(name = "срок кредита", example = "6")
    private Integer term;
    @Schema(name = "имя", example = "firstName")
    private String firstName;
    @Schema(name = "фамилия", example = "lastName")
    private String lastName;
    @Schema(name = "отчество", example = "middleName")
    private String middleName;
    @Schema(name = "адрес электронной почты", example = "email@mail.com")
    private String email;
    @Schema(name = "дата рождения", format = "yyyy-mm-dd", example = "2000-02-23")
    private LocalDate birthdate;
    @Schema(name = "серия паспорта", example = "1234")
    private String passportSeries;
    @Schema(name = "номер паспорта", example = "123456")
    private String passportNumber;

}
