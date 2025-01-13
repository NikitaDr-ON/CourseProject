package com.neoflex.deal.dto;

import com.neoflex.deal.enums.Gender;
import com.neoflex.deal.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FinishRegistrationRequestDto {

    @Schema(description = "гендер", example = "MALE")
    private Gender gender;
    @Schema(description = "женат/замужем, разведен/разведена, вдова/вдовец, холост", example = "MARRIED")
    private MaritalStatus maritalStatus;
    @Schema(description = "сумма выплаты на иждивенца", example = "1000")
    private int dependentAmount;
    @Schema(description = "дата выдачи паспорта", format = "yyyy-mm-dd", example = "2000-02-23")
    private LocalDate passportIssueDate;
    @Schema(description = "кем выдан паспорт", example = "Отделение")
    private String passportIssueBranch;
    private EmploymentDto employment;
    @Schema(description = "номер аккаунта", example = "123")
    private String accountNumber;

}
