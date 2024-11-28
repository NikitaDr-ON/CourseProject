package com.neoflex.CourseProject.dto;

import com.neoflex.CourseProject.dto.enums.Gender;
import com.neoflex.CourseProject.dto.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ScoringDataDto {

    @Schema(name = "amount", example = "20000")
    private BigDecimal amount;
    @Schema(name = "term", example = "6")
    private Integer term;
    @Schema(name = "firstName", example = "firstName")
    private String firstName;
    @Schema(name = "lastName", example = "lastName")
    private String lastName;
    @Schema(name = "middleName", example = "firstname")
    private String middleName;
    @Schema(name = "gender", example = "MALE")
    private Gender gender;
    @Schema(name = "birthdate", format = "yyyy-mm-dd", example = "2000-02-23")
    private LocalDate birthdate;
    @Schema(name = "passportSeries", example = "1234")
    private String passportSeries;
    @Schema(name = "passportNumber", example = "123456")
    private String passportNumber;
    @Schema(name = "passportIssueDate", format = "yyyy-mm-dd", example = "2030-02-23")
    private LocalDate passportIssueDate;
    @Schema(name = "passportIssueBranch", example = "some issue branch")
    private String passportIssueBranch;
    @Schema(name = "maritalStatus", example = "SINGLE")
    private MaritalStatus maritalStatus;
    @Schema(name = "dependentAmount", example = "20000")
    private Integer dependentAmount;
    private EmploymentDto employment;
    @Schema(name = "accountNumber", example = "123")
    private String accountNumber;
    @Schema(name = "isInsuranceEnabled", example = "true")
    private Boolean isInsuranceEnabled;
    @Schema(name = "isSalaryClient", example = "true")
    private Boolean isSalaryClient;

}
