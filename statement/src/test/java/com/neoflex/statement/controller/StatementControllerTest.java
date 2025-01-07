package com.neoflex.statement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.statement.dto.LoanOfferDto;
import com.neoflex.statement.dto.LoanStatementRequestDto;
import com.neoflex.statement.service.PostService;
import com.neoflex.statement.validation.PreScoringValidationService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatementController.class)
@DisplayName("Mock testing the StatementController")
class StatementControllerTest {

    @MockitoBean
    private PreScoringValidationService preScoringValidationServiceImpl;
    @MockitoBean
    private PostService postServiceImpl;

    @Autowired
    MockMvc mvc;

    private LoanStatementRequestDto loanStatementRequestDto;
    private List<LoanOfferDto> loanOfferDtoList;

    @BeforeEach
    void setUp() {
        String namesRegex = "^[A-Za-z]{2,30}";
        String passportSeriesRegex = "^[0-9]{4}";
        String emailRegex = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$";
        String passportNumberRegex = "^[0-9]{6}";
        BigDecimal minimalAmount = BigDecimal.valueOf(20000);
        int minimalTerm = 6;
        int minimalAge = 18;
        int nameMinSymbolsRestriction = 2;
        int nameMaxSymbolsRestriction = 30;
        int countOfPassportSeriesDigits = 4;
        int countOfPassportNumberDigits = 6;

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
    }

    @Test
    void preScoring_allArgsCorrect_ShouldNotThrowException() throws Exception {
        String json = new ObjectMapper().writeValueAsString(loanStatementRequestDto);
        mvc.perform(post("/statement")
                        .content(json)
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void choiceOfOffer() throws Exception {
        String json = new ObjectMapper().writeValueAsString(loanStatementRequestDto);
        mvc.perform(post("/statement/offer")
                        .content(json)
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}