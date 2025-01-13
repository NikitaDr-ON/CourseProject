package com.neoflex.deal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.deal.dto.LoanOfferDto;
import com.neoflex.deal.dto.LoanStatementRequestDto;
import com.neoflex.deal.service.FinishRegistrationService;
import com.neoflex.deal.service.PossibleConditionsService;
import com.neoflex.deal.service.SelectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DealController.class)
class DealControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    private PossibleConditionsService possibleConditionsService;
    @MockitoBean
    private FinishRegistrationService finishRegistrationService;
    @MockitoBean
    private SelectService selectService;

    private LoanStatementRequestDto loanStatementRequestDto;
    private List<LoanOfferDto> loanOfferDtoList;
    private LoanOfferDto loanOfferDto;

    @BeforeEach
    void setUp() {
        loanStatementRequestDto = LoanStatementRequestDto.builder()
                .passportSeries("1231")
                .passportNumber("123123")
                .term(6)
                .email("some@mail.ru")
                .firstName("Ivan")
                .middleName("Ivanovich")
                .lastName("Ivanov")
                .amount(BigDecimal.valueOf(20000))
                .build();
        loanOfferDtoList = new ArrayList<>();
        loanOfferDto = LoanOfferDto.builder()
                .build();
    }

    @Test
    @DisplayName("Mock testing the method calculatingOfPossibleLoanTerms expected status 200")
    void calculatingOfPossibleLoanTerms_expectedStatus200() throws Exception {
        when(possibleConditionsService.getPossibleConditions(loanStatementRequestDto)).thenReturn(loanOfferDtoList);
        String json = new ObjectMapper().writeValueAsString(loanStatementRequestDto);
        mvc.perform(post("/deal/statement")
                        .content(json)
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Mock testing the method selectOffer expected status 200")
    void selectOffer() throws Exception {
        String json = new ObjectMapper().writeValueAsString(loanOfferDto);
        mvc.perform(post("/deal/offer/select")
                        .content(json)
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}