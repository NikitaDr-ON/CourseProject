package com.neoflex.deal.dto;

import com.neoflex.deal.enums.EmploymentStatus;
import com.neoflex.deal.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDto {

    @Schema(description = "статус работника", example = "SELF_EMPLOYED")
    private EmploymentStatus employmentStatus;
    @Schema(description = "ИНН работника", example = "123")
    private String employerINN;
    @Schema(description = "зарплата", example = "9000")
    private BigDecimal salary;
    @Schema(description = "должность", example = "MIDDLE_MANAGER")
    private Position position;
    @Schema(description = "общий опыт работы", example = "20")
    private Integer workExperienceTotal;
    @Schema(description = "текущий опыт работы", example = "20")
    private Integer workExperienceCurrent;

}
