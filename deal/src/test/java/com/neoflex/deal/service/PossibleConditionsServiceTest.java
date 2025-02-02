package com.neoflex.deal.service;

import com.neoflex.deal.dto.LoanOfferDto;
import com.neoflex.deal.dto.LoanStatementRequestDto;
import com.neoflex.deal.repository.ClientRepository;
import com.neoflex.deal.repository.StatementRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mock testing the PossibleConditionsService")
class PossibleConditionsServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private StatementRepository statementRepository;

    @Mock
    private PostServiceImpl postService;

    @InjectMocks
    private PossibleConditionsServiceImpl possibleConditionsService;

    private LoanStatementRequestDto loanStatementRequestDto;
    List<LoanOfferDto> loanOfferDtoList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        LoanOfferDto loanOfferDto = LoanOfferDto.builder()
                .isInsuranceEnabled(Boolean.TRUE)
                .isSalaryClient(Boolean.TRUE)
                .build();
        loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(200000))
                .term(6)
                .birthdate(LocalDate.now())
                .email("some@mail.ru")
                .firstName("Ivan")
                .middleName("Ivanovich")
                .lastName("Ivanov")
                .passportNumber("123123")
                .passportSeries("1231")
                .build();
        for (int i = 0; i < 4; i++) {
            loanOfferDtoList.add(loanOfferDto);
        }
    }

    @Test
    @DisplayName("Mock testing the method getPossibleConditions should not throw exception")
    void getPossibleConditions_shouldNotThrowException() {
        when(postService.postOffers(loanStatementRequestDto)).thenReturn(loanOfferDtoList);
        Assertions.assertDoesNotThrow(() -> possibleConditionsService.getPossibleConditions(loanStatementRequestDto));

    }

}