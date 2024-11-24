package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.LoanOfferDto;
import com.neoflex.CourseProject.pojo.PreScoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PreScoringServiceImpl implements PreScoringService {

    private final PreScoringProperties preScoringProperties;

    @Autowired
    public PreScoringServiceImpl(PreScoringProperties preScoringProperties){
        this.preScoringProperties = preScoringProperties;
    }

     private List<LoanOfferDto> creatingVariationsOfLoan(){
        List<LoanOfferDto> variations = new ArrayList<>();
        variations.add(LoanOfferDto.builder().isInsuranceEnabled(Boolean.FALSE).isSalaryClient(Boolean.FALSE).build());
        variations.add(LoanOfferDto.builder().isInsuranceEnabled(Boolean.FALSE).isSalaryClient(Boolean.TRUE).build());
        variations.add(LoanOfferDto.builder().isInsuranceEnabled(Boolean.TRUE).isSalaryClient(Boolean.FALSE).build());
        variations.add(LoanOfferDto.builder().isInsuranceEnabled(Boolean.TRUE).isSalaryClient(Boolean.TRUE).build());
        log.info("all variations LoanOfferDto: {}", variations);
        return variations;
    }

    @Override
     public List<LoanOfferDto> resultsOfPreScoring(BigDecimal requestedAmount){
         log.info("requestedAmount input: {}", requestedAmount);
         List<LoanOfferDto> loanOffer = creatingVariationsOfLoan();
         for (LoanOfferDto offer : loanOffer ) {
             preScoringOfLoanOffer(offer, requestedAmount);
         }
         sortLoanOffers(loanOffer);
         log.info(loanOffer.toString());
         log.info("result of preScoring: {}", loanOffer);
         return loanOffer;
    }

     private void preScoringOfLoanOffer(LoanOfferDto loanOffer, BigDecimal requestedAmount){
         log.info("LoanOfferDto and BigDecimal input: {}, {}",loanOffer, requestedAmount);
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
         loanOffer.setRate(BigDecimal.valueOf(finalRate+preScoringProperties.getDefaultRate()));
         log.info("rate calculation result: " + loanOffer.getRate());
     }

     private void sortLoanOffers(List<LoanOfferDto> loanOffer){
         log.info("list LoanOfferDto before sort: {}", loanOffer);
        loanOffer.sort(Comparator.comparing(LoanOfferDto::getRate).reversed());
         log.info("list LoanOfferDto after sort: {}", loanOffer);
    }
}
