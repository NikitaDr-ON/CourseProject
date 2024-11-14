package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.LoanOfferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@PropertySource("classpath:application.properties")
public class PreScoringService {

    @Value("${LoanOfferDto.rate}")
     int defaultRate;
     List<LoanOfferDto> creatingVariationsOfLoan(){
        List<LoanOfferDto> variations = new ArrayList<>();
        variations.add(new LoanOfferDto(false,false));
        variations.add(new LoanOfferDto(false,true));
        variations.add(new LoanOfferDto(true,false));
        variations.add(new LoanOfferDto(true,true));
        return variations;
    }

     public List<LoanOfferDto> resultsOfPreScoring(BigDecimal requestedAmount){
        List<LoanOfferDto> loanOffer = creatingVariationsOfLoan();
        for (LoanOfferDto offer : loanOffer )
            preScoringOfLoanOffer(offer,requestedAmount);
        sortLoanOffers(loanOffer);
        return loanOffer;
    }

     void preScoringOfLoanOffer(LoanOfferDto loanOffer, BigDecimal requestedAmount){
        System.out.println(defaultRate);
         BigDecimal paymentForInsurance = new BigDecimal(100000);
        int pointsForInsurance = 3;
        int pointsForSalaryClient = 1;
        int finalRate = 0;
        if(loanOffer.getIsInsuranceEnabled()) {
            loanOffer.setTotalAmount(requestedAmount.add(paymentForInsurance));
            finalRate-=pointsForInsurance;
        }else {
            finalRate+=pointsForInsurance;
        }
        if(loanOffer.getIsSalaryClient()) {
            finalRate-=pointsForSalaryClient;
        }else {
            finalRate+=pointsForSalaryClient;
        }
        loanOffer.setRate(BigDecimal.valueOf(finalRate+defaultRate));
    }

     void sortLoanOffers(List<LoanOfferDto> loanOffer){
        loanOffer.sort(Comparator.comparing(LoanOfferDto::getRate));
    }
}
