package com.neoflex.CourseProject.dto;

import com.neoflex.CourseProject.dto.enums.EmploymentStatus;
import com.neoflex.CourseProject.dto.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDto {

    @Schema(name = "employmentStatus", example = "SELF_EMPLOYED")
    private EmploymentStatus employmentStatus;
    @Schema(name = "employerINN", example = "123")
    private String employerINN;
    @Schema(name = "salary", example = "9000")
    private BigDecimal salary;
    @Schema(name = "position", example = "MIDDLE_MANAGER")
    private Position position;
    @Schema(name = "workExperienceTotal", example = "20")
    private Integer workExperienceTotal;
    @Schema(name = "workExperienceCurrent", example = "20")
    private Integer workExperienceCurrent;

}
