package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.EmploymentDto;
import com.neoflex.CourseProject.dto.ScoringDataDto;
import com.neoflex.CourseProject.dto.enums.EmploymentStatus;
import com.neoflex.CourseProject.dto.enums.Gender;
import com.neoflex.CourseProject.dto.enums.MaritalStatus;
import com.neoflex.CourseProject.dto.enums.Position;
import com.neoflex.CourseProject.exception.ValidationException;
import com.neoflex.CourseProject.pojo.ScoringProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mock testing the ScoringService")
class ScoringServiceTest {

    @Mock
    private CalculatingService calculatingService;

    @Mock
    private RatingService ratingService;

    @Mock
    ScoringValidationService scoringValidationService;

    @Mock
    ScoringProperties scoringProperties;

    @InjectMocks
    private ScoringServiceImpl implScoringService;

    @Test
    void scoring_rateShouldBe17(){
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
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

        when(scoringProperties.getDefaultRate()).thenReturn(BigDecimal.valueOf(20));
        Assertions.assertDoesNotThrow(()-> implScoringService.scoring(scoringDataDto));
        Assertions.assertEquals(BigDecimal.valueOf(17), implScoringService.scoring(scoringDataDto).getRate());
    }

    @Test
    void scoring_ShouldThrowExceptionAtWorkExperienceCurrent(){
        ReflectionTestUtils.setField(implScoringService, "defaultRate", 20);
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
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
        Assertions.assertThrows(ValidationException.class,()-> implScoringService.scoring(scoringDataDto));
    }

    @Test
    void scoring_ShouldThrowExceptionAtWorkExperienceTotal(){
        ReflectionTestUtils.setField(implScoringService, "defaultRate", 20);
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
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
        Assertions.assertThrows(ValidationException.class,()-> implScoringService.scoring(scoringDataDto));
    }

    @Test
    void scoring_rateShouldBe20(){
        ReflectionTestUtils.setField(implScoringService, "defaultRate", 20);
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
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
        Assertions.assertDoesNotThrow(()-> implScoringService.scoring(scoringDataDto));
        Assertions.assertEquals(BigDecimal.valueOf(20), implScoringService.scoring(scoringDataDto).getRate());
    }

    @Test
    void scoring_rateShouldBe27(){
        ReflectionTestUtils.setField(implScoringService, "defaultRate", 20);
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
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
        Assertions.assertDoesNotThrow(()-> implScoringService.scoring(scoringDataDto));
        Assertions.assertEquals(BigDecimal.valueOf(27), implScoringService.scoring(scoringDataDto).getRate());
    }

    @Test
    void scoring_rateShouldBeMale17(){
        ReflectionTestUtils.setField(implScoringService, "defaultRate", 20);
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(1980,9,23))
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
        Assertions.assertDoesNotThrow(()-> implScoringService.scoring(scoringDataDto));
        Assertions.assertEquals(BigDecimal.valueOf(17), implScoringService.scoring(scoringDataDto).getRate());
    }

    @Test
    void scoring_rateShouldBe16(){
        ReflectionTestUtils.setField(implScoringService, "defaultRate", 20);
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(1980,9,23))
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
        Assertions.assertDoesNotThrow(()-> implScoringService.scoring(scoringDataDto));
        Assertions.assertEquals(BigDecimal.valueOf(16), implScoringService.scoring(scoringDataDto).getRate());
    }

    @Test
    void scoring_ShouldThrowExceptionAtEmploymentStatus(){
        ReflectionTestUtils.setField(implScoringService, "defaultRate", 20);
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
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
        Assertions.assertThrows(ValidationException.class,()-> implScoringService.scoring(scoringDataDto));
    }

    @Test
    void scoring_ShouldThrowExceptionAtLoanMoreThan24MonthsSalary(){
        ReflectionTestUtils.setField(implScoringService, "defaultRate", 20);
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2002,9,23))
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
        Assertions.assertThrows(ValidationException.class,()-> implScoringService.scoring(scoringDataDto));
    }

    @Test
    void scoring_ShouldThrowExceptionAtIsAgeOver20AndUnder60(){
        ReflectionTestUtils.setField(implScoringService, "defaultRate", 20);
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .birthdate(LocalDate.of(2010,9,23))
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
        Assertions.assertThrows(ValidationException.class,()-> implScoringService.scoring(scoringDataDto));
    }
}