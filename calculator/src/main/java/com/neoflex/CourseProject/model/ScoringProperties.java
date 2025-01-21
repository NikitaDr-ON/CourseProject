package com.neoflex.CourseProject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "scoring")
public class ScoringProperties {

    private final BigDecimal percent;
    private final BigDecimal daysInLeapYear;
    private final BigDecimal daysInYear;
    private final BigDecimal pointsForSelfEmployed;
    private final BigDecimal pointsForBusinessOwner;
    private final BigDecimal pointsForMiddleManager;
    private final BigDecimal pointsForTopManager;
    private final BigDecimal pointsForSingle;
    private final BigDecimal pointsForMarried;
    private final int minimumAge;
    private final int maximumAge;
    private final BigDecimal countOfSalaries;
    private final int minimumAgeForMale;
    private final int maximumAgeForMale;
    private final BigDecimal pointsForMaleBetween30and50;
    private final int minimumAgeForFemale;
    private final int maximumAgeForFemale;
    private final BigDecimal pointsForFemaleBetween32and60;
    private final BigDecimal pointsForNonBinary;
    private final int minimumTotalWorkExperience;
    private final int minimumCurrentWorkExperience;
    private final BigDecimal defaultRate;

}
