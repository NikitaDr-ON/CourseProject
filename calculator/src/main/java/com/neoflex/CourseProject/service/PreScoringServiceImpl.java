package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.CreditDto;
import com.neoflex.CourseProject.dto.LoanOfferDto;
import com.neoflex.CourseProject.dto.LoanStatementRequestDto;
import com.neoflex.CourseProject.model.PreScoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class PreScoringServiceImpl implements PreScoringService {

    private final PreScoringProperties preScoringProperties;

    private final CalculationService calculationServiceImpl;

    @Autowired
    public PreScoringServiceImpl(PreScoringProperties preScoringProperties, CalculationService calculationServiceImpl) {
        this.preScoringProperties = preScoringProperties;
        this.calculationServiceImpl = calculationServiceImpl;
    }

    @Override
    public List<LoanOfferDto> resultsOfPreScoring(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("requestedAmount входные параметры: {}", loanStatementRequestDto);
        CreditDto creditDto;
        List<LoanOfferDto> loanOffer = creatingVariationsOfLoan();
        for (LoanOfferDto offer : loanOffer) {
            offer.setTotalAmount(loanStatementRequestDto.getAmount());
            preScoringOfLoanOffer(offer, loanStatementRequestDto.getAmount());
            offer.setTerm(loanStatementRequestDto.getTerm());
            offer.setRequestedAmount(loanStatementRequestDto.getAmount());
            creditDto = createCreditDto(offer);
            calculationServiceImpl.calculationOfPayments(creditDto);
            offer.setMonthlyPayment(creditDto.getPaymentSchedule().get(0).getTotalPayment());
        }
        sortLoanOffers(loanOffer);
        log.info("Результат предварительного расчета: {}", loanOffer);
        return loanOffer;
    }

    private CreditDto createCreditDto(LoanOfferDto loanOffer) {
        return CreditDto.builder()
                .term(loanOffer.getTerm())
                .amount(loanOffer.getTotalAmount())
                .rate(loanOffer.getRate())
                .build();
    }

    private List<LoanOfferDto> creatingVariationsOfLoan() {
        List<LoanOfferDto> variations = new ArrayList<>();
        variations.add(LoanOfferDto.builder().isInsuranceEnabled(Boolean.FALSE).isSalaryClient(Boolean.FALSE).build());
        variations.add(LoanOfferDto.builder().isInsuranceEnabled(Boolean.FALSE).isSalaryClient(Boolean.TRUE).build());
        variations.add(LoanOfferDto.builder().isInsuranceEnabled(Boolean.TRUE).isSalaryClient(Boolean.FALSE).build());
        variations.add(LoanOfferDto.builder().isInsuranceEnabled(Boolean.TRUE).isSalaryClient(Boolean.TRUE).build());
        log.info("Все варианты LoanOfferDto: {}", variations);
        return variations;
    }

    private void preScoringOfLoanOffer(LoanOfferDto loanOffer, BigDecimal requestedAmount) {
        log.info("preScoringOfLoanOffer входные параметры: {}, {}", loanOffer, requestedAmount);
        BigDecimal paymentForInsurance = new BigDecimal(preScoringProperties.getInsurancePrice());
        int finalRate = 0;
        if (loanOffer.getIsInsuranceEnabled()) {
            loanOffer.setTotalAmount(requestedAmount.add(paymentForInsurance));
            finalRate -= preScoringProperties.getPointsForInsurance();
        } else {
            finalRate += preScoringProperties.getPointsForInsurance();
        }
        if (loanOffer.getIsSalaryClient()) {
            finalRate -= preScoringProperties.getPointsForSalaryClient();
        } else {
            finalRate += preScoringProperties.getPointsForSalaryClient();
        }
        loanOffer.setRate(BigDecimal.valueOf(finalRate + preScoringProperties.getDefaultRate()));
        log.info("Результат расчета preScoringOfLoanOffer: {}", loanOffer.getRate());
    }

    private void sortLoanOffers(List<LoanOfferDto> loanOffer) {
        log.info("Лист LoanOfferDto до сортировки: {}", loanOffer);
        loanOffer.sort(Comparator.comparing(LoanOfferDto::getRate).reversed());
        log.info("Лист LoanOfferDto после сортировки: {}", loanOffer);
    }
}
