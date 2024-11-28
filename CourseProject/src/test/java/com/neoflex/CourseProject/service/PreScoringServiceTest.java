package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.LoanStatementRequestDto;
import com.neoflex.CourseProject.model.PreScoringProperties;
import com.neoflex.CourseProject.model.ScoringProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mock testing the PreScoringService")
class PreScoringServiceTest {

    @Mock
    private PreScoringProperties preScoringProperties;

    @Mock
    private ScoringProperties scoringProperties;

    @Mock
    private CalculationService calculationServiceImpl;

    @InjectMocks
    private PreScoringService preScoringServiceImpl = new PreScoringServiceImpl(preScoringProperties,
            calculationServiceImpl);

    private LoanStatementRequestDto loanStatementRequestDto;

    @BeforeEach
    void setUp() {
        int pointsForInsurance = 3;
        int pointsForSalaryClient = 1;
        int insurancePrice = 20000;
        int defaultRate = 20;
        BigDecimal daysInYear = BigDecimal.valueOf(365);
        BigDecimal pointsForSelfEmployed = BigDecimal.valueOf(2);
        BigDecimal pointsForBusinessOwner = BigDecimal.valueOf(1);
        BigDecimal pointsForMiddleManager = BigDecimal.valueOf(2);
        BigDecimal pointsForTopManager = BigDecimal.valueOf(3);
        BigDecimal pointsForSingle = BigDecimal.valueOf(1);
        BigDecimal pointsForMarried = BigDecimal.valueOf(3);
        int minimumAge = 20;
        int maximumAge = 60;
        BigDecimal countOfSalaries = BigDecimal.valueOf(24);
        int minimumAgeForMale = 30;
        int maximumAgeForMale = 55;
        BigDecimal pointsForMaleBetween30and50 = BigDecimal.valueOf(3);
        int minimumAgeForFemale = 32;
        int maximumAgeForFemale = 60;
        BigDecimal pointsForFemaleBetween32and60 = BigDecimal.valueOf(3);
        BigDecimal pointsForNonBinary = BigDecimal.valueOf(7);
        int minimumTotalWorkExperience = 18;
        int minimumCurrentWorkExperience = 3;
        BigDecimal daysInLeapYear = BigDecimal.valueOf(366);
        BigDecimal percent = BigDecimal.valueOf(100);
        BigDecimal defaultRateScoringProperties = BigDecimal.valueOf(20);
        scoringProperties = new ScoringProperties(percent, daysInLeapYear, daysInYear, pointsForSelfEmployed,
                pointsForBusinessOwner
                , pointsForMiddleManager, pointsForTopManager, pointsForSingle, pointsForMarried, minimumAge,
                maximumAge,
                countOfSalaries, minimumAgeForMale, maximumAgeForMale, pointsForMaleBetween30and50, minimumAgeForFemale,
                maximumAgeForFemale, pointsForFemaleBetween32and60, pointsForNonBinary, minimumTotalWorkExperience,
                minimumCurrentWorkExperience, defaultRateScoringProperties);
        preScoringProperties = new PreScoringProperties(pointsForInsurance, pointsForSalaryClient, insurancePrice,
                defaultRate);
        calculationServiceImpl = new CalculationServiceImpl(scoringProperties);
        preScoringServiceImpl = new PreScoringServiceImpl(preScoringProperties, calculationServiceImpl);
        loanStatementRequestDto = LoanStatementRequestDto.builder()
                .term(6)
                .amount(BigDecimal.valueOf(20000))
                .build();
        ReflectionTestUtils.setField(preScoringServiceImpl, "preScoringProperties", preScoringProperties);
        ReflectionTestUtils.setField(preScoringServiceImpl, "calculationServiceImpl", calculationServiceImpl);
    }

    @Test
    @DisplayName("Mock testing the method resultsOfPreScoring for the number of returned objects")
    void resultsOfPreScoring_shouldReturnArrayOf4() {
        Assertions.assertEquals(4, preScoringServiceImpl.resultsOfPreScoring(loanStatementRequestDto).size());
    }

    @Test
    @DisplayName("Mock testing the method resultsOfPreScoring for equality of objects")
    void resultsOfPreScoring_shouldBeEquals() {
        Assertions.assertEquals(BigDecimal.valueOf(24),
                preScoringServiceImpl.resultsOfPreScoring(loanStatementRequestDto).get(0).getRate());
        Assertions.assertEquals(BigDecimal.valueOf(22),
                preScoringServiceImpl.resultsOfPreScoring(loanStatementRequestDto).get(1).getRate());
        Assertions.assertEquals(BigDecimal.valueOf(18),
                preScoringServiceImpl.resultsOfPreScoring(loanStatementRequestDto).get(2).getRate());
        Assertions.assertEquals(BigDecimal.valueOf(16),
                preScoringServiceImpl.resultsOfPreScoring(loanStatementRequestDto).get(3).getRate());
    }
}