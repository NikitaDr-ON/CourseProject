package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.CreditDto;
import com.neoflex.CourseProject.dto.ScoringDataDto;
import com.neoflex.CourseProject.model.ScoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
public class RatingServiceImpl implements RatingService {

    private final ScoringProperties scoringProperties;

    @Autowired
    public RatingServiceImpl(ScoringProperties scoringProperties) {
        this.scoringProperties = scoringProperties;
    }

    public void scoringFinalRate(ScoringDataDto scoringDataDto, CreditDto creditDto) {
        log.info("scoringFinalRate входные параметры: {},{}", scoringDataDto, creditDto);
        ratingForEmploymentStatus(scoringDataDto, creditDto);
        ratingJobPosition(scoringDataDto, creditDto);
        ratingForMaritalStatus(scoringDataDto, creditDto);
        ratingForGender(scoringDataDto, creditDto);
        log.info("Процентная ставка рассчитана");
    }

    private void ratingForEmploymentStatus(ScoringDataDto scoringDataDto, CreditDto creditDto) {
        log.info("ratingForEmploymentStatus входные параметры: {},{}", scoringDataDto, creditDto);
        BigDecimal currentRate = creditDto.getRate();
        switch (scoringDataDto.getEmployment().getEmploymentStatus()) {
            case SELF_EMPLOYED:
                creditDto.setRate(currentRate.add(scoringProperties.getPointsForSelfEmployed()));
                break;
            case BUSINESS_OWNER:
                creditDto.setRate(currentRate.add(scoringProperties.getPointsForBusinessOwner()));
                break;
        }
        log.info("ratingForEmploymentStatus результат расчета: {}", creditDto.getRate());
    }

    private void ratingJobPosition(ScoringDataDto scoringDataDto, CreditDto creditDto) {
        log.info("ratingJobPosition входные параметры: {},{}", scoringDataDto, creditDto);
        BigDecimal currentRate = creditDto.getRate();
        switch (scoringDataDto.getEmployment().getPosition()) {
            case MIDDLE_MANAGER:
                creditDto.setRate(currentRate.subtract(scoringProperties.getPointsForMiddleManager()));
                break;
            case TOP_MANAGER:
                creditDto.setRate(currentRate.subtract(scoringProperties.getPointsForTopManager()));
                break;
        }
        log.info("ratingJobPosition результат расчета: {}", creditDto.getRate());
    }

    private void ratingForMaritalStatus(ScoringDataDto scoringDataDto, CreditDto creditDto) {
        log.info("ratingForMaritalStatus входные параметры: {},{}", scoringDataDto, creditDto);
        BigDecimal currentRate = creditDto.getRate();
        switch (scoringDataDto.getMaritalStatus()) {
            case SINGLE:
                creditDto.setRate(currentRate.add(scoringProperties.getPointsForSingle()));
                break;
            case MARRIED:
                creditDto.setRate(currentRate.subtract(scoringProperties.getPointsForMarried()));
                break;
        }
        log.info("ratingForMaritalStatus результат расчета: {}", creditDto.getRate());
    }

    private void ratingForGender(ScoringDataDto scoringDataDto, CreditDto creditDto) {
        log.info("ratingForGender входные параметры : {},{}", scoringDataDto, creditDto);
        BigDecimal currentRate = creditDto.getRate();
        switch (scoringDataDto.getGender()) {
            case MALE:
                if (LocalDate.now().getYear() - scoringDataDto.getBirthdate()
                        .getYear() >= scoringProperties.getMinimumAgeForMale()
                        && LocalDate.now().getYear() - scoringDataDto.getBirthdate()
                        .getYear() <= scoringProperties.getMaximumAgeForMale()) {
                    creditDto.setRate(currentRate.subtract(scoringProperties.getPointsForMaleBetween30and50()));
                }
                break;
            case FEMALE:
                if (LocalDate.now().getYear() - scoringDataDto.getBirthdate()
                        .getYear() >= scoringProperties.getMinimumAgeForFemale()
                        && LocalDate.now().getYear() - scoringDataDto.getBirthdate()
                        .getYear() <= scoringProperties.getMaximumAgeForFemale()) {
                    creditDto.setRate(currentRate.subtract(scoringProperties.getPointsForFemaleBetween32and60()));
                }
                break;
            case NON_BINARY:
                creditDto.setRate(currentRate.add(scoringProperties.getPointsForNonBinary()));
        }
        log.info("ratingForGender результат расчета: {}", creditDto.getRate());
    }
}
