package com.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class LoanStatementRequestDto {

    @Schema(name = "amount", example = "20000")
    private BigDecimal amount;
    @Schema(name = "term", example = "6")
    private Integer term;
    @Schema(name = "firstName", example = "firstName")
    private String firstName;
    @Schema(name = "lastName", example = "lastName")
    private String lastName;
    @Schema(name = "middleName", example = "middleName")
    private String middleName;
    @Schema(name = "email", example = "email@mail.com")
    private String email;
    @Schema(name = "birthdate", format = "yyyy-mm-dd", example = "2000-02-23")
    private LocalDate birthdate;
    @Schema(name = "passportSeries", example = "1234")
    private String passportSeries;
    @Schema(name = "passportNumber", example = "123456")
    private String passportNumber;

}
