package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.EmploymentDto;
import com.neoflex.CourseProject.dto.ScoringDataDto;
import com.neoflex.CourseProject.dto.enums.EmploymentStatus;
import com.neoflex.CourseProject.dto.enums.Gender;
import com.neoflex.CourseProject.dto.enums.MaritalStatus;
import com.neoflex.CourseProject.dto.enums.Position;
import com.neoflex.CourseProject.exception.ValidationException;
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
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mock testing the ScoringService")
class ScoringServiceTest {

    @Mock
    private CalculationServiceImpl calculationServiceImpl;

    @Mock
    private RatingServiceImpl ratingServiceImpl;

    @Mock
    private ScoringValidationServiceImpl scoringValidationServiceImpl;

    @Mock
    ScoringProperties scoringProperties;

    @InjectMocks
    private ScoringServiceImpl implScoringService;

    @BeforeEach
    void beforeEach() {
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
        ratingServiceImpl = new RatingServiceImpl(scoringProperties);
        calculationServiceImpl = new CalculationServiceImpl(scoringProperties);
        scoringValidationServiceImpl = new ScoringValidationServiceImpl(scoringProperties);
        ReflectionTestUtils.setField(implScoringService, "scoringProperties", scoringProperties);
        ReflectionTestUtils.setField(implScoringService, "scoringValidationServiceImpl", scoringValidationServiceImpl);
        ReflectionTestUtils.setField(implScoringService, "ratingServiceImpl", ratingServiceImpl);
        ReflectionTestUtils.setField(implScoringService, "calculationServiceImpl", calculationServiceImpl);
    }

    @Test
    @DisplayName("rate should be equals 17 male married middle manager self employed")
    void givenMaleSelfEmployedMiddleManagerMarried_whenCallScoring_rateShouldBeEquals17() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .firstName("FirstName")
                .middleName("middleName")
                .lastName("lastName")
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .gender(Gender.MALE)
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .salary(BigDecimal.valueOf(100000))
                        .position(Position.MIDDLE_MANAGER)
                        .workExperienceCurrent(20)
                        .workExperienceTotal(20)
                        .build())
                .maritalStatus(MaritalStatus.MARRIED)
                .term(6)
                .build();
        Assertions.assertDoesNotThrow(() -> implScoringService.scoring(scoringDataDto));
        Assertions.assertEquals(BigDecimal.valueOf(17), implScoringService.scoring(scoringDataDto).getRate());
    }

    @Test
    @DisplayName("work experience current less than 3 result incorrect")
    void givenWorkExperienceCurrentLessThanThreeMonths_whenCallScoring_shouldThrowValidationException() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .gender(Gender.MALE)
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .salary(BigDecimal.valueOf(9000))
                        .position(Position.MIDDLE_MANAGER)
                        .workExperienceCurrent(1)
                        .workExperienceTotal(20)
                        .build())
                .maritalStatus(MaritalStatus.MARRIED)
                .term(6)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> implScoringService.scoring(scoringDataDto));
    }

    @Test
    @DisplayName("work experience total less than 18 result incorrect")
    void givenWorkExperienceTotalLessThanEighteenMonths_whenCallScoring_shouldThrowValidationException() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .gender(Gender.MALE)
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .salary(BigDecimal.valueOf(9000))
                        .position(Position.MIDDLE_MANAGER)
                        .workExperienceCurrent(20)
                        .workExperienceTotal(1)
                        .build())
                .maritalStatus(MaritalStatus.MARRIED)
                .term(6)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> implScoringService.scoring(scoringDataDto));
    }

    @Test
    @DisplayName("rate should be equals 20 female single top manager self employed")
    void givenFemaleSelfEmployedTopManagerSingle_whenCallScoring_rateShouldBeEquals20() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .gender(Gender.FEMALE)
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .salary(BigDecimal.valueOf(9000))
                        .position(Position.TOP_MANAGER)
                        .workExperienceCurrent(20)
                        .workExperienceTotal(20)
                        .build())
                .maritalStatus(MaritalStatus.SINGLE)
                .term(6)
                .build();
        Assertions.assertDoesNotThrow(() -> implScoringService.scoring(scoringDataDto));
        Assertions.assertEquals(BigDecimal.valueOf(20), implScoringService.scoring(scoringDataDto).getRate());
    }

    @Test
    @DisplayName("rate should be equals 27 non binary single top manager self employed")
    void givenNonBinarySelfEmployedTopManagerSingle_whenCallScoring_rateShouldBeEquals27() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .gender(Gender.NON_BINARY)
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .salary(BigDecimal.valueOf(9000))
                        .position(Position.TOP_MANAGER)
                        .workExperienceCurrent(20)
                        .workExperienceTotal(20)
                        .build())
                .maritalStatus(MaritalStatus.SINGLE)
                .term(6)
                .build();
        Assertions.assertDoesNotThrow(() -> implScoringService.scoring(scoringDataDto));
        Assertions.assertEquals(BigDecimal.valueOf(27), implScoringService.scoring(scoringDataDto).getRate());
    }

    @Test
    @DisplayName("rate should be equals 17 male single top manager self employed")
    void givenMaleSelfEmployedTopManagerSingle_whenCallScoring_rateShouldBeEquals17() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(1980, 9, 23))
                .gender(Gender.MALE)
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .salary(BigDecimal.valueOf(9000))
                        .position(Position.TOP_MANAGER)
                        .workExperienceCurrent(20)
                        .workExperienceTotal(20)
                        .build())
                .maritalStatus(MaritalStatus.SINGLE)
                .term(6)
                .build();
        Assertions.assertDoesNotThrow(() -> implScoringService.scoring(scoringDataDto));
        Assertions.assertEquals(BigDecimal.valueOf(17), implScoringService.scoring(scoringDataDto).getRate());
    }

    @Test
    @DisplayName("rate should be equals 16 female single business owner self employed")
    void givenFemaleBusinessOwnerTopManagerSingle_whenCallScoring_rateShouldBeEquals16() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(1980, 9, 23))
                .gender(Gender.FEMALE)
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                        .salary(BigDecimal.valueOf(9000))
                        .position(Position.TOP_MANAGER)
                        .workExperienceCurrent(20)
                        .workExperienceTotal(20)
                        .build())
                .maritalStatus(MaritalStatus.SINGLE)
                .term(6)
                .build();
        Assertions.assertDoesNotThrow(() -> implScoringService.scoring(scoringDataDto));
        Assertions.assertEquals(BigDecimal.valueOf(16), implScoringService.scoring(scoringDataDto).getRate());
    }

    @Test
    @DisplayName("unemployed result incorrect")
    void givenUnemployed_whenCallScoring_shouldThrowValidationException() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .gender(Gender.MALE)
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.UNEMPLOYED)
                        .salary(BigDecimal.valueOf(9000))
                        .position(Position.MIDDLE_MANAGER)
                        .workExperienceCurrent(20)
                        .workExperienceTotal(20)
                        .build())
                .maritalStatus(MaritalStatus.MARRIED)
                .term(6)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> implScoringService.scoring(scoringDataDto));
    }

    @Test
    @DisplayName("loan amount more than 24 salaries result incorrect")
    void givenLoanAmountBiggerThen24SalariesAmount_whenCallScoring_shouldThrowValidationException() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002, 9, 23))
                .gender(Gender.MALE)
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .salary(BigDecimal.valueOf(1000))
                        .position(Position.MIDDLE_MANAGER)
                        .workExperienceCurrent(20)
                        .workExperienceTotal(20)
                        .build())
                .maritalStatus(MaritalStatus.MARRIED)
                .term(6)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> implScoringService.scoring(scoringDataDto));
    }

    @Test
    @DisplayName("age under 20 result incorrect")
    void givenBirthdayCorrespondsToAgeUnderThanTwenty_whenCallScoring_shouldThrowValidationException() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2010, 9, 23))
                .gender(Gender.MALE)
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .salary(BigDecimal.valueOf(100000))
                        .position(Position.MIDDLE_MANAGER)
                        .workExperienceCurrent(20)
                        .workExperienceTotal(20)
                        .build())
                .maritalStatus(MaritalStatus.MARRIED)
                .term(6)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> implScoringService.scoring(scoringDataDto));
    }
}