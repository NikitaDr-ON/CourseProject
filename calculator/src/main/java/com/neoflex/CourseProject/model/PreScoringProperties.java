package com.neoflex.CourseProject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "pre-scoring")
public class PreScoringProperties {

    private final int pointsForInsurance;
    private final int pointsForSalaryClient;
    private final int insurancePrice;
    private final int defaultRate;

}
