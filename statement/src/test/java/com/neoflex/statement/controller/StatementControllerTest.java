package com.neoflex.statement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.statement.dto.LoanOfferDto;
import com.neoflex.statement.dto.LoanStatementRequestDto;
import com.neoflex.statement.exception.ValidationException;
import com.neoflex.statement.service.PostService;
import com.neoflex.statement.validation.PreScoringValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatementController.class)
@AutoConfigureMockMvc
@DisplayName("Mock testing the StatementController")
class StatementControllerTest {

    @MockitoBean
    private PreScoringValidationService preScoringValidationServiceImpl;
    @MockitoBean
    private PostService postServiceImpl;

    @Autowired
    MockMvc mvc;

    private LoanStatementRequestDto loanStatementRequestDto;
    private LoanOfferDto loanOfferDto;

    @BeforeEach
    void setUp() {
        loanStatementRequestDto = LoanStatementRequestDto.builder()
                .passportSeries("1231")
                .passportNumber("1231")
                .term(6)
                .email("some@mail.ru")
                .firstName("Ivan")
                .middleName("Ivanovich")
                .lastName("Ivanov")
                .amount(BigDecimal.valueOf(20000))
                .build();
        LoanOfferDto.builder().build();
    }

    @Test
    void preScoring_ShouldThrowValidationException() throws Exception {
        when(postServiceImpl.postDealStatement(any())).thenThrow(ValidationException.class);
        String json = new ObjectMapper().writeValueAsString(loanStatementRequestDto);
        mvc.perform(post("/statement")
                        .characterEncoding("utf-8")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void preScoring_allArgsCorrect_ShouldNotThrowException() throws Exception {
        when(postServiceImpl.postDealStatement(any())).thenThrow(ValidationException.class);
        String json = new ObjectMapper().writeValueAsString(loanStatementRequestDto);
        mvc.perform(post("/statement")
                        .characterEncoding("utf-8")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void choiceOfOffer_allArgsCorrect_ShouldNotThrowException() throws Exception {
        String json = new ObjectMapper().writeValueAsString(loanStatementRequestDto);
        mvc.perform(post("/statement/offer")
                        .content(json)
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}