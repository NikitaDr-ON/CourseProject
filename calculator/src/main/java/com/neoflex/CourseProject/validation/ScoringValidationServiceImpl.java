package com.neoflex.CourseProject.validation;

import com.neoflex.CourseProject.dto.ScoringDataDto;
import com.neoflex.CourseProject.dto.enums.EmploymentStatus;
import com.neoflex.CourseProject.exception.ValidationException;
import com.neoflex.CourseProject.model.ScoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class ScoringValidationServiceImpl implements ScoringValidationService{

    private final ScoringProperties scoringProperties;

    @Autowired
    public ScoringValidationServiceImpl(ScoringProperties scoringProperties) {
        this.scoringProperties = scoringProperties;
    }

    public void resultsOfValidation(ScoringDataDto scoringDataDto) {
        log.info("resultsOfValidation входные параметры: {}", scoringDataDto);
        isUnemployed(scoringDataDto);
        loanMoreThan24MonthsSalary(scoringDataDto);
        isAgeOver20AndUnder60(scoringDataDto);
        checkWorkExperience(scoringDataDto);
        log.info("валидация завершена");
    }

    private void isUnemployed(ScoringDataDto scoringDataDto) {
        if (scoringDataDto.getEmployment().getEmploymentStatus() == EmploymentStatus.UNEMPLOYED) {
            log.info("должность клиента не прошла валидацию {}", scoringDataDto.getEmployment().getEmploymentStatus());
            throw new ValidationException("клиент должен быть трудоустроен");
        }
    }

    private void loanMoreThan24MonthsSalary(ScoringDataDto scoringDataDto) {
        if (scoringDataDto.getEmployment().getSalary().multiply(scoringProperties.getCountOfSalaries()).
                compareTo(scoringDataDto.getAmount()) < 0) {
            log.info("сумма кредита {} больше чем сумма двадцати четырех зарплат {}", scoringDataDto.getAmount(),
                    scoringDataDto.getEmployment().getSalary().multiply(scoringProperties.getCountOfSalaries()));
            throw new ValidationException(
                    "сумма кредита" + scoringDataDto.getAmount()
                            + "должна быть меньше чем сумма двадцати четырех зарплат "
                            + scoringDataDto.getEmployment().getSalary()
                            .multiply(scoringProperties.getCountOfSalaries()));
        }
    }

    private void isAgeOver20AndUnder60(ScoringDataDto scoringDataDto) {
        if (LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear() < scoringProperties.getMinimumAge()) {
            log.info("возраст клиента {} не прошел валидацию на минимальный возраст",
                    LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear());
            throw new ValidationException("возраст должен быть больше " + scoringProperties.getMinimumAge() + " лет");
        }
        if (LocalDate.now().getYear() - scoringDataDto.getBirthdate()
                .getYear() > scoringProperties.getMaximumAge()) {
            log.info("возраст клиента {} не прошел валидацию на максимальный возраст",
                    LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear());
            throw new ValidationException("возраст должен быть меньше " + scoringProperties.getMaximumAge() + " лет");
        }
    }

    private void checkWorkExperience(ScoringDataDto scoringDataDto) {
        if (scoringDataDto.getEmployment()
                .getWorkExperienceTotal() < scoringProperties.getMinimumTotalWorkExperience()) {
            log.info("общий стаж клиента {} не прошел валидацию на количество рабочих месяцев",
                    scoringDataDto.getEmployment().getWorkExperienceTotal());
            throw new ValidationException("общий стаж работы должен быть больше "
                    + scoringProperties.getMinimumTotalWorkExperience() + " месяцев");
        }
        if (scoringDataDto.getEmployment()
                .getWorkExperienceCurrent() < scoringProperties.getMinimumCurrentWorkExperience()) {
            log.info("текущий стаж клиента {} не прошел валидацию на количество рабочих месяцев",
                    scoringDataDto.getEmployment().getWorkExperienceCurrent());
            throw new ValidationException("текущий стаж работы должен быть больше "
                    + scoringProperties.getMinimumCurrentWorkExperience() + " месяцев");
        }
    }

}
