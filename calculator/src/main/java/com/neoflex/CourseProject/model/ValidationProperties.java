package com.neoflex.CourseProject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "validation")
public class ValidationProperties {

    private final String namesRegex;
    private final String passportSeriesRegex;
    private final String emailRegex;
    private final String passportNumberRegex;
    private final BigDecimal minimalAmount;
    private final int minimalTerm;
    private final int minimalAge;
    private final int nameMinSymbolsRestriction;
    private final int nameMaxSymbolsRestriction;
    private final int countOfPassportSeriesDigits;
    private final int countOfPassportNumberDigits;

}
