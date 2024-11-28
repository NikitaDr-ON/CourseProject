package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.CreditDto;
import com.neoflex.CourseProject.dto.ScoringDataDto;
import com.neoflex.CourseProject.model.ScoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ScoringServiceImpl implements ScoringService {

    private final ScoringValidationService scoringValidationServiceImpl;
    private final RatingService ratingServiceImpl;
    private final ScoringProperties scoringProperties;
    private final CalculationService calculationServiceImpl;

    @Autowired
    public ScoringServiceImpl(ScoringValidationService scoringValidationServiceImpl, RatingService ratingServiceImpl,
                              ScoringProperties scoringProperties, CalculationService calculationServiceImpl) {
        this.scoringValidationServiceImpl = scoringValidationServiceImpl;
        this.ratingServiceImpl = ratingServiceImpl;
        this.scoringProperties = scoringProperties;
        this.calculationServiceImpl = calculationServiceImpl;
    }

    @Override
    public CreditDto scoring(ScoringDataDto scoringDataDto) {
        log.info("scoring входные параметры: {}", scoringDataDto);
        CreditDto creditDto = CreditDto.builder()
                .rate(scoringProperties.getDefaultRate())
                .term(scoringDataDto.getTerm())
                .isInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDto.getIsSalaryClient())
                .amount(scoringDataDto.getAmount())
                .build();
        scoringValidationServiceImpl.resultsOfValidation(scoringDataDto);
        ratingServiceImpl.scoringFinalRate(scoringDataDto, creditDto);
        calculationServiceImpl.calculationOfPayments(creditDto);
        log.info("результат расчета: {}", creditDto);
        return creditDto;
    }
}
