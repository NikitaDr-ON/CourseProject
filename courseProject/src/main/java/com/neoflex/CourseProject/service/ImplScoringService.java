package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.CreditDto;
import com.neoflex.CourseProject.dto.PaymentScheduleElementDto;
import com.neoflex.CourseProject.dto.ScoringDataDto;
import com.neoflex.CourseProject.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
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
public class ImplScoringService implements Scoring {

    private static final int DAYS_IN_YEAR = 366;
    private static final BigDecimal POINTS_FOR_SELF_EMPLOYMENT = BigDecimal.valueOf(2);
    private static final BigDecimal POINTS_FOR_BUSINESS_OWNER = BigDecimal.valueOf(1);
    private static final BigDecimal POINTS_FOR_MIDDLE_MANAGER = BigDecimal.valueOf(2);
    private static final BigDecimal POINTS_FOR_TOP_MANAGER = BigDecimal.valueOf(3);
    private static final BigDecimal POINTS_FOR_DIVORCED = BigDecimal.valueOf(1);
    private static final BigDecimal POINTS_FOR_MARRIED = BigDecimal.valueOf(3);
    private static final int MINIMUM_AGE = 20;
    private static final int MAXIMUM_AGE = 60;
    private static final BigDecimal COUNT_OF_SALARIES = BigDecimal.valueOf(24);
    private static final int MINIMUM_AGE_FOR_MALE = 30;
    private static final int MAXIMUM_AGE_FOR_MALE = 55;
    private static final BigDecimal POINTS_FOR_MALE_BETWEEN_30_AND_55 = BigDecimal.valueOf(3);
    private static final int MINIMUM_AGE_FOR_FEMALE = 32;
    private static final int MAXIMUM_AGE_FOR_FEMALE = 60;
    private static final BigDecimal POINTS_FOR_FEMALE_BETWEEN_32_AND_60 = BigDecimal.valueOf(3);
    private static final BigDecimal POINTS_FOR_NON_BINARY = BigDecimal.valueOf(7);
    private static final int MINIMUM_TOTAL_WORK_EXPERIENCE = 18;
    private static final int MINIMUM_CURRENT_WORK_EXPERIENCE = 3;

    @Value("${ScoringAndPreScoring.rate}")
    private int defaultRate;

    @Override
    public CreditDto scoring(ScoringDataDto scoringDataDto){
        log.info("scoring input: " + scoringDataDto.toString());
        CreditDto creditDto = CreditDto.builder()
                .rate(BigDecimal.valueOf(defaultRate))
                .term(scoringDataDto.getTerm())
                .isInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDto.getIsSalaryClient())
                .amount(scoringDataDto.getAmount())
                .build();
        scoringFinalRate(scoringDataDto, creditDto);
        calculationOfPayments(creditDto);
        log.info("scoring output: " + creditDto.toString());
        return creditDto;
    }


    private void scoringFinalRate(ScoringDataDto scoringDataDto, CreditDto creditDto){
        log.info("scoringFinalRate input: " + scoringDataDto.toString() + " " + creditDto.toString());
        ratingForEmploymentStatus(scoringDataDto,creditDto);
        ratingJobPosition(scoringDataDto,creditDto);
        loanMoreThan24MonthsSalary(scoringDataDto);
        ratingForMaritalStatus(scoringDataDto,creditDto);
        isAgeOver20AndUnder60(scoringDataDto);
        ratingForGender(scoringDataDto,creditDto);
        checkWorkExperience(scoringDataDto);
        log.info("final rate calculation completed successfully");
    }

    private void ratingForEmploymentStatus(ScoringDataDto scoringDataDto, CreditDto creditDto){
        log.info("ratingForEmploymentStatus input: " + scoringDataDto.toString() + " " + creditDto.toString());
        BigDecimal currentRate = creditDto.getRate();
        switch (scoringDataDto.getEmployment().getEmploymentStatus()) {
            case SELF_EMPLOYMENT:
                creditDto.setRate(currentRate.add(POINTS_FOR_SELF_EMPLOYMENT));
                log.info(creditDto.getRate().toString());
                break;
            case BUSINESS_OWNER:
                creditDto.setRate(currentRate.add(POINTS_FOR_BUSINESS_OWNER));
                log.info(creditDto.getRate().toString());
                break;
            case UNEMPLOYMENT:
                log.info("validation exception not required status of employee");
                throw new ValidationException("not required status of employee");
        }
    }

    private void ratingJobPosition(ScoringDataDto scoringDataDto, CreditDto creditDto){
        log.info("ratingJobPosition input: " + scoringDataDto.toString() + " " + creditDto.toString());
        BigDecimal currentRate = creditDto.getRate();
        switch (scoringDataDto.getEmployment().getPosition()) {
            case MIDDLE_MANAGER:
                creditDto.setRate(currentRate.subtract(POINTS_FOR_MIDDLE_MANAGER));
                log.info(creditDto.getRate().toString());
                break;
            case TOP_MANAGER:
                creditDto.setRate(currentRate.subtract(POINTS_FOR_TOP_MANAGER));
                log.info(creditDto.getRate().toString());
                break;
        }
    }

    private void loanMoreThan24MonthsSalary(ScoringDataDto scoringDataDto){
        if(scoringDataDto.getEmployment().getSalary().multiply(COUNT_OF_SALARIES).compareTo(scoringDataDto.getAmount()) < 0) {
            log.info("the loan amount is more than the sum of salaries for 24 months");
            throw new ValidationException("the loan amount is more than the sum of salaries for 24 months");
        }
    }

    private void ratingForMaritalStatus(ScoringDataDto scoringDataDto, CreditDto creditDto){
        log.info("ratingForMaritalStatus input: " + scoringDataDto.toString() + " " + creditDto.toString());
        BigDecimal currentRate = creditDto.getRate();
        switch (scoringDataDto.getMaritalStatus()) {
            case DIVORCED:
                creditDto.setRate(currentRate.add(POINTS_FOR_DIVORCED));
                log.info(creditDto.getRate().toString());
                break;
            case MARRIED:
                creditDto.setRate(currentRate.subtract(POINTS_FOR_MARRIED));
                log.info(creditDto.getRate().toString());
                break;
        }
    }

    private void isAgeOver20AndUnder60(ScoringDataDto scoringDataDto){
        if(LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear() < MINIMUM_AGE ||LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear() > MAXIMUM_AGE){
            throw new ValidationException("age less than 20 or over 60");
        }
    }

    private void ratingForGender(ScoringDataDto scoringDataDto, CreditDto creditDto) {
        log.info("ratingForGender input: " + scoringDataDto.toString() + " " + creditDto.toString());
        BigDecimal currentRate = creditDto.getRate();
        switch (scoringDataDto.getGender()) {
            case MALE:
                if(LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear() >= MINIMUM_AGE_FOR_MALE && LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear() <= MAXIMUM_AGE_FOR_MALE){
                    creditDto.setRate(currentRate.subtract(POINTS_FOR_MALE_BETWEEN_30_AND_55));
                    log.info(creditDto.getRate().toString());
                }
                break;
            case FEMALE:
                if(LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear() >= MINIMUM_AGE_FOR_FEMALE && LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear() <= MAXIMUM_AGE_FOR_FEMALE){
                    creditDto.setRate(currentRate.subtract(POINTS_FOR_FEMALE_BETWEEN_32_AND_60));
                    log.info(creditDto.getRate().toString());
                }
                break;
            case NON_BINARY:
                creditDto.setRate(currentRate.add(POINTS_FOR_NON_BINARY));
                log.info(creditDto.getRate().toString());
        }
    }

    private void checkWorkExperience(ScoringDataDto scoringDataDto){
        if(scoringDataDto.getEmployment().getWorkExperienceTotal() < MINIMUM_TOTAL_WORK_EXPERIENCE){
            log.info("validation exception total work experience less than 18 months");
            throw new ValidationException("total work experience less than 18 months");
        }
        if(scoringDataDto.getEmployment().getWorkExperienceCurrent() < MINIMUM_CURRENT_WORK_EXPERIENCE){
            log.info("validation exception current work experience less than 18 months");
            throw new ValidationException("current work experience less than 3 months");
        }
    }

    private void calculationOfPayments(CreditDto creditDto){
        log.info("calculationOfPayments input: " + creditDto.toString());
        BigDecimal remainder = creditDto.getAmount();
        BigDecimal basePayment = (creditDto.getAmount().divide(BigDecimal.valueOf(creditDto.getTerm()),2, RoundingMode.HALF_UP));
        BigDecimal debetPayment;
        BigDecimal totalPayment = BigDecimal.valueOf(0);
        BigDecimal percent = (creditDto.getRate().divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP));
        BigDecimal amountPayment = BigDecimal.valueOf(0);
        List<PaymentScheduleElementDto> paymentScheduleElementDto = new ArrayList<>();
        for(int i=0;i<creditDto.getTerm();i++){
            int daysInMonth = LocalDate.now().plusMonths(i).lengthOfMonth();
            debetPayment = remainder.multiply(percent).multiply(BigDecimal.valueOf(daysInMonth)).divide(BigDecimal.valueOf(DAYS_IN_YEAR), 2, RoundingMode.HALF_UP);
            totalPayment = basePayment.add(debetPayment);
            amountPayment=amountPayment.add(totalPayment);
            paymentScheduleElementDto.add(PaymentScheduleElementDto.builder()
                    .number(i)
                    .date(LocalDate.now().plusMonths(i))
                    .interestPayment(basePayment)
                    .debtPayment(debetPayment)
                    .totalPayment(basePayment.add(debetPayment))
                    .remainingDebt(remainder)
                    .build());
            remainder=remainder.subtract(basePayment);
        }
        creditDto.setPsk(amountPayment);
        creditDto.setPaymentSchedule(paymentScheduleElementDto);
        creditDto.setMonthlyPayment(basePayment);
        log.info("calculation of payments is complete");
    }
}
