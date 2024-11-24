package com.neoflex.CourseProject.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "validation")
@ConfigurationPropertiesScan
public class ValidationProperties {
    private final String namesRegex;
    private final String passportSeriesRegex;
    private final String emailRegex;
    private final String passportNumberRegex;
    private final BigDecimal minimalAmount;
    private final int minimalTerm;
    private final int minimalAge;
}
