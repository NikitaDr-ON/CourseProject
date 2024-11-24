package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.CreditDto;
import com.neoflex.CourseProject.dto.ScoringDataDto;
import com.neoflex.CourseProject.pojo.ScoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
public class RatingService {

    private final ScoringProperties scoringProperties;

    @Autowired
    public RatingService(ScoringProperties scoringProperties){
        this.scoringProperties = scoringProperties;
    }

    public void scoringFinalRate(ScoringDataDto scoringDataDto, CreditDto creditDto){
        log.info("scoringFinalRate input: " + scoringDataDto.toString() + " " + creditDto.toString());
        ratingForEmploymentStatus(scoringDataDto,creditDto);
        ratingJobPosition(scoringDataDto,creditDto);
        ratingForMaritalStatus(scoringDataDto,creditDto);
        ratingForGender(scoringDataDto,creditDto);
        log.info("final rate calculation completed successfully");
    }

    private void ratingForEmploymentStatus(ScoringDataDto scoringDataDto, CreditDto creditDto){
        log.info("ratingForEmploymentStatus input: " + scoringDataDto.toString() + " " + creditDto.toString());
        BigDecimal currentRate = creditDto.getRate();
        switch (scoringDataDto.getEmployment().getEmploymentStatus()) {
            case SELF_EMPLOYED:
                creditDto.setRate(currentRate.add(scoringProperties.getPointsForSelfEmployed()));
                log.info(creditDto.getRate().toString());
                break;
            case BUSINESS_OWNER:
                creditDto.setRate(currentRate.add(scoringProperties.getPointsForBusinessOwner()));
                log.info(creditDto.getRate().toString());
                break;
        }
    }

    private void ratingJobPosition(ScoringDataDto scoringDataDto, CreditDto creditDto){
        log.info("ratingJobPosition input: " + scoringDataDto.toString() + " " + creditDto.toString());
        BigDecimal currentRate = creditDto.getRate();
        switch (scoringDataDto.getEmployment().getPosition()) {
            case MIDDLE_MANAGER:
                creditDto.setRate(currentRate.subtract(scoringProperties.getPointsForMiddleManager()));
                log.info(creditDto.getRate().toString());
                break;
            case TOP_MANAGER:
                creditDto.setRate(currentRate.subtract(scoringProperties.getPointsForTopManager()));
                log.info(creditDto.getRate().toString());
                break;
        }
    }



    private void ratingForMaritalStatus(ScoringDataDto scoringDataDto, CreditDto creditDto){
        log.info("ratingForMaritalStatus input: " + scoringDataDto.toString() + " " + creditDto.toString());
        BigDecimal currentRate = creditDto.getRate();
        switch (scoringDataDto.getMaritalStatus()) {
            case SINGLE:
                creditDto.setRate(currentRate.add(scoringProperties.getPointsForSingle()));
                log.info(creditDto.getRate().toString());
                break;
            case MARRIED:
                creditDto.setRate(currentRate.subtract(scoringProperties.getPointsForMarried()));
                log.info(creditDto.getRate().toString());
                break;
        }
    }


    private void ratingForGender(ScoringDataDto scoringDataDto, CreditDto creditDto) {
        log.info("ratingForGender input: " + scoringDataDto.toString() + " " + creditDto.toString());
        BigDecimal currentRate = creditDto.getRate();
        switch (scoringDataDto.getGender()) {
            case MALE:
                if (LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear() >= scoringProperties.getMinimumAgeForMale()
                        && LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear() <= scoringProperties.getMaximumAgeForMale()){
                    creditDto.setRate(currentRate.subtract(scoringProperties.getPointsForMaleBetween30and50()));
                    log.info(creditDto.getRate().toString());
                }
                break;
            case FEMALE:
                if (LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear() >= scoringProperties.getMinimumAgeForFemale()
                        && LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear() <= scoringProperties.getMaximumAgeForFemale()){
                    creditDto.setRate(currentRate.subtract(scoringProperties.getPointsForFemaleBetween32and60()));
                    log.info(creditDto.getRate().toString());
                }
                break;
            case NON_BINARY:
                creditDto.setRate(currentRate.add(scoringProperties.getPointsForNonBinary()));
                log.info(creditDto.getRate().toString());
        }
    }
}
