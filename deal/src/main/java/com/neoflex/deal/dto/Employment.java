package com.neoflex.deal.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Employment {

    private String employer_inn;
    private BigDecimal salary;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;

}
