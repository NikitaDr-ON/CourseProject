package com.neoflex.CourseProject.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "pre-scoring")
@ConfigurationPropertiesScan
public class PreScoringProperties {

    private final int pointsForInsurance;
    private final int pointsForSalaryClient;
    private final int insurancePrice;
    private final int defaultRate;

}
