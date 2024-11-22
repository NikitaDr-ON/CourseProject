package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.LoanOfferDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@PropertySource("classpath:application.properties")
public class ImplPreScoringService implements PreScoring {

    private static final int POINTS_FOR_INSURANCE = 3;
    private static final int POINTS_FOR_SALARY_CLIENT = 1;
    private static final int INSURANCE_PRICE = 100000;

    @Value("${ScoringAndPreScoring.rate}")
     private int defaultRate;

     private List<LoanOfferDto> creatingVariationsOfLoan(){
        List<LoanOfferDto> variations = new ArrayList<>();
        variations.add(LoanOfferDto.builder().isInsuranceEnabled(Boolean.FALSE).isSalaryClient(Boolean.FALSE).build());
        variations.add(LoanOfferDto.builder().isInsuranceEnabled(Boolean.FALSE).isSalaryClient(Boolean.TRUE).build());
        variations.add(LoanOfferDto.builder().isInsuranceEnabled(Boolean.TRUE).isSalaryClient(Boolean.FALSE).build());
        variations.add(LoanOfferDto.builder().isInsuranceEnabled(Boolean.TRUE).isSalaryClient(Boolean.TRUE).build());
        log.info("creatingVariationsOfLoan result: " + variations.toString());
        return variations;
    }

    @Override
     public List<LoanOfferDto> resultsOfPreScoring(BigDecimal requestedAmount){
         log.info("resultsOfPreScoring input: " +requestedAmount.toString());
         List<LoanOfferDto> loanOffer = creatingVariationsOfLoan();
         for (LoanOfferDto offer : loanOffer ) {
             preScoringOfLoanOffer(offer, requestedAmount);
         }
         sortLoanOffers(loanOffer);
         log.info(loanOffer.toString());
         log.info("resultsOfPreScoring result: " + loanOffer.toString());
         return loanOffer;
    }

     void preScoringOfLoanOffer(LoanOfferDto loanOffer, BigDecimal requestedAmount){
         log.info("preScoringOfLoanOffer input: "+loanOffer.toString()+ "\n"
                 + requestedAmount.toString());
         BigDecimal paymentForInsurance = new BigDecimal(INSURANCE_PRICE);
         int finalRate = 0;
         if(loanOffer.getIsInsuranceEnabled()) {
             loanOffer.setTotalAmount(requestedAmount.add(paymentForInsurance));
             finalRate-=POINTS_FOR_INSURANCE;
         }else {
             finalRate+=POINTS_FOR_INSURANCE;
         }
         if(loanOffer.getIsSalaryClient()) {
             finalRate-=POINTS_FOR_SALARY_CLIENT;
         }else {
             finalRate+=POINTS_FOR_SALARY_CLIENT;
         }
         loanOffer.setRate(BigDecimal.valueOf(finalRate+defaultRate));
         log.info("preScoringOfLoanOffer result: " + loanOffer.getRate());
     }

     void sortLoanOffers(List<LoanOfferDto> loanOffer){
         log.info("preScoringOfLoanOffer input: " + loanOffer.toString());
        loanOffer.sort(Comparator.comparing(LoanOfferDto::getRate));
         log.info("preScoringOfLoanOffer result: " + loanOffer.toString());
    }
}
