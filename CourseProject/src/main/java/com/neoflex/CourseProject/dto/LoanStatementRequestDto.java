package com.neoflex.CourseProject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class LoanStatementRequestDto {

  @Schema(name = "amount", example = "20000", required = true)
  private BigDecimal amount;
  @Schema(name = "term", example = "6", required = true)
  private Integer term;
  @Schema(name = "firstName", example = "firstName", required = true)
  private String firstName;
  @Schema(name = "lastName", example = "lastName", required = true)
  private String lastName;
  @Schema(name = "middleName", example = "middleName", required = true)
  private String middleName;
  @Schema(name = "email", example = "email@mail.com", required = true)
  private String email;
  private LocalDate birthdate;
  @Schema(name = "passportSeries", example = "1234", required = true)
  private String passportSeries;
  @Schema(name = "passportNumber", example = "123456", required = true)
  private String passportNumber;

}
