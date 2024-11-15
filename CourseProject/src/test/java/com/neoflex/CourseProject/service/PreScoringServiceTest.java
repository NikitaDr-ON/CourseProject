package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.LoanOfferDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


class PreScoringServiceTest {

    private PreScoringService preScoringService;
    @BeforeEach
    void setUp() {
        preScoringService = new PreScoringService();
    }

    @Test
    void creatingVariationsOfLoan() {
        List<LoanOfferDto> variations = preScoringService.creatingVariationsOfLoan();
        List<LoanOfferDto> shouldBeSame = new ArrayList<>();
        shouldBeSame.add(new LoanOfferDto(false,false));
        shouldBeSame.add(new LoanOfferDto(false,true));
        shouldBeSame.add(new LoanOfferDto(true,false));
        shouldBeSame.add(new LoanOfferDto(true,true));
        assertIterableEquals(variations, shouldBeSame);
    }

    @Test
    void preScoringOfLoanOffer_shouldReturnRate4() {
        LoanOfferDto offer = new LoanOfferDto(false, false); // Например, без страховки, зарплатный клиент
        preScoringService.preScoringOfLoanOffer(offer, BigDecimal.valueOf(100000));
        assertEquals(BigDecimal.valueOf(4), offer.getRate());
    }

    @Test
    void preScoringOfLoanOffer_shouldReturnRate2() {
        LoanOfferDto offer = new LoanOfferDto(false, true); // Например, без страховки, зарплатный клиент
        preScoringService.preScoringOfLoanOffer(offer,BigDecimal.valueOf(100000));
        assertEquals(BigDecimal.valueOf(2), offer.getRate());
    }
    @Test
    void preScoringOfLoanOffer_shouldReturnRateMinus2() {
        LoanOfferDto offer = new LoanOfferDto(true, false); // Например, без страховки, зарплатный клиент
        preScoringService.preScoringOfLoanOffer(offer,BigDecimal.valueOf(100000));
        assertEquals(BigDecimal.valueOf(-2), offer.getRate());
    }

    @Test
    void preScoringOfLoanOffer_shouldReturnRateMinus4() {
        LoanOfferDto offer = new LoanOfferDto(true, true); // Например, без страховки, зарплатный клиент
        preScoringService.preScoringOfLoanOffer(offer, BigDecimal.valueOf(100000));
        assertEquals(BigDecimal.valueOf(-4), offer.getRate());
    }

    @Test
    void sortLoanOffers(){
        List<LoanOfferDto> variations = new ArrayList<>();
        variations.add(new LoanOfferDto(false,false));
        variations.add(new LoanOfferDto(false,true));
        variations.add(new LoanOfferDto(true,false));
        variations.add(new LoanOfferDto(true,true));
        for(LoanOfferDto variant : variations)
            preScoringService.preScoringOfLoanOffer(variant,BigDecimal.valueOf(100000));
        preScoringService.sortLoanOffers(variations);
        List<BigDecimal> rateFromList = variations.stream().map(LoanOfferDto::getRate).toList();
        List<BigDecimal> shouldBe = new ArrayList<>();
        shouldBe.add(BigDecimal.valueOf(-4));
        shouldBe.add(BigDecimal.valueOf(-2));
        shouldBe.add(BigDecimal.valueOf(2));
        shouldBe.add(BigDecimal.valueOf(4));

        assertArrayEquals(new List[]{shouldBe}, new List[]{rateFromList});
    }

    @Test
    void resultsOfPreScoring() {
    }
}