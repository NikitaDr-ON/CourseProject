package com.neoflex.deal.service;

import com.neoflex.deal.dto.*;
import com.neoflex.deal.entity.Client;
import com.neoflex.deal.entity.Statement;
import com.neoflex.deal.enums.EmploymentStatus;
import com.neoflex.deal.enums.Gender;
import com.neoflex.deal.enums.MaritalStatus;
import com.neoflex.deal.enums.Position;
import com.neoflex.deal.exception.NotFoundException;
import com.neoflex.deal.model.PostServiceProperties;
import com.neoflex.deal.repository.CreditRepository;
import com.neoflex.deal.repository.StatementRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mock testing the FinishRegistrationService")
class FinishRegistrationServiceTest {

    @Mock
    private StatementRepository statementRepository;

    @Mock
    private PostServiceImpl postServiceImpl;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private PostServiceProperties postServiceProperties;

    @InjectMocks
    private FinishRegistrationServiceImpl finishRegistrationService;

    private Statement statement;
    private CreditDto creditDto;
    private FinishRegistrationRequestDto finishRegistrationRequestDto;

    @BeforeEach
    void setUp() {
        Passport passport = Passport.builder()
                .number("123123")
                .series("1231")
                .issue_branch("asdasd")
                .issue_date(LocalDate.now())
                .build();
        Client client = Client.builder()
                .firstName("Ivan")
                .middleName("Ivanovich")
                .lastName("Ivanov")
                .email("ivan@mail.ru")
                .dependentAmount(123)
                .gender(Gender.MALE)
                .birthdate(LocalDate.now())
                .maritalStatus(MaritalStatus.MARRIED)
                .passport(passport)
                .build();
        statement = Statement.builder()
                .statementId(UUID.randomUUID())
                .client(client)
                .build();
        creditDto = CreditDto.builder()
                .term(12)
                .psk(BigDecimal.valueOf(200000))
                .rate(BigDecimal.valueOf(14))
                .isInsuranceEnabled(Boolean.TRUE)
                .isSalaryClient(Boolean.TRUE)
                .build();
        finishRegistrationRequestDto = FinishRegistrationRequestDto.builder()
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(100)
                .passportIssueDate(LocalDate.now())
                .passportIssueBranch("SomeWhere")
                .employment(EmploymentDto.builder()
                        .employerINN("123123123123")
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .position(Position.MIDDLE_MANAGER)
                        .salary(BigDecimal.valueOf(100000))
                        .workExperienceCurrent(20)
                        .workExperienceTotal(20)
                        .build())
                .accountNumber("123123123")
                .build();
    }

    @BeforeTestMethod

    @Test
    @DisplayName("Mock testing the method finishRegistration should not throw exception")
    void finishRegistration_shouldNotThrowException() {
        when(postServiceImpl.postLoanOfferDto(any(ScoringDataDto.class))).thenReturn(creditDto);
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.of(statement));
        Assertions.assertDoesNotThrow(() -> finishRegistrationService.finishRegistration(finishRegistrationRequestDto,
                String.valueOf(statement.getStatementId())));
    }

    @Test
    @DisplayName("statementRepository findById doesn't find statement by id should throw exception")
    void finishRegistration_shouldThrowException() {
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () ->
                finishRegistrationService.finishRegistration(finishRegistrationRequestDto,
                        String.valueOf(statement.getStatementId())));
    }
}
