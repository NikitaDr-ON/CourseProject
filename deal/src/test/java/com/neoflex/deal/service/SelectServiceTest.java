package com.neoflex.deal.service;

import com.neoflex.deal.dto.LoanOfferDto;
import com.neoflex.deal.dto.StatusHistory;
import com.neoflex.deal.entity.Statement;
import com.neoflex.deal.exception.NotFoundException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mock testing the SelectService")
class SelectServiceTest {

    @Mock
    private StatementRepository statementRepository;

    @InjectMocks
    private SelectServiceImpl selectService;

    private LoanOfferDto loanOfferDto;
    private Statement statement;

    @BeforeEach
    void setUp() {
        loanOfferDto = LoanOfferDto.builder()
                .isSalaryClient(Boolean.TRUE)
                .isInsuranceEnabled(Boolean.TRUE)
                .monthlyPayment(BigDecimal.valueOf(1000))
                .rate(BigDecimal.valueOf(14))
                .requestedAmount(BigDecimal.valueOf(15000))
                .statementId(UUID.randomUUID())
                .build();
        List<StatusHistory> statusHistories = new ArrayList<>();
        statement = Statement.builder()
                .statementId(UUID.randomUUID())
                .statusHistory(statusHistories)
                .build();
    }

    @Test
    @DisplayName("Mock testing the method selectStatement should not throw exception")
    void selectStatement_shouldNotThrowException() {
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(statement));
        Assertions.assertDoesNotThrow(() ->  selectService.selectStatement(loanOfferDto));
    }

    @Test
    @DisplayName("if statementRepository doesn't find if of statement should throw exception")
    void selectStatement_shouldThrowException() {
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () ->
                selectService.selectStatement(loanOfferDto));
    }
}