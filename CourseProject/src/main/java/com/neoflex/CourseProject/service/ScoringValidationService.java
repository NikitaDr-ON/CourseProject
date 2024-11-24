package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.ScoringDataDto;
import com.neoflex.CourseProject.dto.enums.EmploymentStatus;
import com.neoflex.CourseProject.exception.ValidationException;
import com.neoflex.CourseProject.pojo.ScoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class ScoringValidationService {

    private final ScoringProperties scoringProperties;

    @Autowired
    public ScoringValidationService(ScoringProperties scoringProperties){
        this.scoringProperties = scoringProperties;
    }

    public void resultsOfValidation(ScoringDataDto scoringDataDto) {
        isUnemployed(scoringDataDto);
        loanMoreThan24MonthsSalary(scoringDataDto);
        isAgeOver20AndUnder60(scoringDataDto);
        checkWorkExperience(scoringDataDto);
    }

    private void isUnemployed(ScoringDataDto scoringDataDto){
        if (scoringDataDto.getEmployment().getEmploymentStatus() == EmploymentStatus.UNEMPLOYED){
            log.info("user is unemployed");
            throw new ValidationException("user is unemployed");
        }
    }

    private void loanMoreThan24MonthsSalary(ScoringDataDto scoringDataDto){
        if (scoringDataDto.getEmployment().getSalary().multiply(scoringProperties.getCountOfSalaries()).
                compareTo(scoringDataDto.getAmount()) < 0) {
            log.info("the loan amount is more than the sum of salaries for 24 months");
            throw new ValidationException("the loan amount is more than the sum of salaries for 24 months");
        }
    }

    private void isAgeOver20AndUnder60(ScoringDataDto scoringDataDto){
        if(LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear() < scoringProperties.getMinimumAge()
                ||LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear() > scoringProperties.getMaximumAge()){
            throw new ValidationException("age less than 20 or over 60");
        }
    }

    private void checkWorkExperience(ScoringDataDto scoringDataDto){
        if (scoringDataDto.getEmployment().getWorkExperienceTotal() < scoringProperties.getMinimumTotalWorkExperience()){
            log.info("validation exception total work experience less than 18 months");
            throw new ValidationException("total work experience less than 18 months");
        }
        if (scoringDataDto.getEmployment().getWorkExperienceCurrent() < scoringProperties.getMinimumCurrentWorkExperience()){
            log.info("validation exception current work experience less than 18 months");
            throw new ValidationException("current work experience less than 3 months");
        }
    }

}
