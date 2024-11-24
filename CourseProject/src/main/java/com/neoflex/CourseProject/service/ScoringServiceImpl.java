package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.CreditDto;
import com.neoflex.CourseProject.dto.PaymentScheduleElementDto;
import com.neoflex.CourseProject.dto.ScoringDataDto;
import com.neoflex.CourseProject.exception.ValidationException;
import com.neoflex.CourseProject.pojo.ScoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@PropertySource("classpath:application.properties")
public class ScoringServiceImpl implements ScoringService {

    private final ScoringValidationService scoringValidationService;
    private final RatingService ratingService;
    private final ScoringProperties scoringProperties;
    private final CalculatingService calculatingService;

    @Autowired
    public ScoringServiceImpl(ScoringValidationService scoringValidationService,RatingService ratingService,
                              ScoringProperties scoringProperties, CalculatingService calculatingService){
        this.scoringValidationService = scoringValidationService;
        this.ratingService = ratingService;
        this.scoringProperties = scoringProperties;
        this.calculatingService = calculatingService;
    }

    @Override
    public CreditDto scoring(ScoringDataDto scoringDataDto){
        log.info("scoring input: " + scoringDataDto.toString());
        CreditDto creditDto = CreditDto.builder()
                .rate(scoringProperties.getDefaultRate())
                .term(scoringDataDto.getTerm())
                .isInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDto.getIsSalaryClient())
                .amount(scoringDataDto.getAmount())
                .build();
        scoringValidationService.resultsOfValidation(scoringDataDto);
        ratingService.scoringFinalRate(scoringDataDto, creditDto);
        calculatingService.calculationOfPayments(creditDto);
        log.info("scoring output: " + creditDto.toString());
        return creditDto;
    }
}
