package com.neoflex.deal.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Employment {

    private String employer_inn;
    private BigDecimal salary;
    private int work_experience_total;
    private int work_experience_current;

}
