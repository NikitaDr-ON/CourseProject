package com.neoflex.deal.service;

import com.neoflex.deal.dto.CreditDto;
import com.neoflex.deal.dto.FinishRegistrationRequestDto;
import com.neoflex.deal.dto.ScoringDataDto;
import com.neoflex.deal.entity.Credit;
import com.neoflex.deal.entity.Statement;
import com.neoflex.deal.enums.CreditStatus;
import com.neoflex.deal.exception.NotFoundException;
import com.neoflex.deal.repository.CreditRepository;
import com.neoflex.deal.repository.StatementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FinishRegistrationServiceImpl implements FinishRegistrationService {

    private final StatementRepository statementRepository;
    private final PostServiceImpl postServiceImpl;
    private final CreditRepository creditRepository;

    @Autowired
    public FinishRegistrationServiceImpl(StatementRepository statementRepository, PostServiceImpl postServiceImpl,
                                         CreditRepository creditRepository) {
        this.statementRepository = statementRepository;
        this.postServiceImpl = postServiceImpl;
        this.creditRepository = creditRepository;
    }

    public void finishRegistration(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId) {
        log.info("FinishRegistrationServiceImpl начало регистрации");
        Statement statement = getStatement(statementId);
        ScoringDataDto scoringDataDto = fillTheScoringDataDto(statement, finishRegistrationRequestDto);
        CreditDto creditDto = postServiceImpl.postLoanOfferDto(scoringDataDto);
        Credit credit = createCreditEntity(creditDto);
        creditRepository.save(credit);
        log.info("FinishRegistrationServiceImpl Credit сохранен в бд");
        statement.setCredit(credit);
        log.info("FinishRegistrationServiceImpl Statement обновлен");
        statementRepository.save(statement);
        log.info("FinishRegistrationServiceImpl завершение регистрации");
    }

    private Statement getStatement(String statementId) {
        log.info("FinishRegistrationServiceImpl поиск заявки по id: {}", statementId);
        Statement statement = statementRepository.findById(UUID.fromString(statementId))
                .orElseThrow(() -> {
                    log.info("FinishRegistrationServiceImpl заявки по id: {} не найдено", statementId);
                    return new NotFoundException("заявка не найдена");
                });
        log.info("FinishRegistrationServiceImpl найдена заявка: {}", statement);
        return statement;
    }

    private ScoringDataDto fillTheScoringDataDto(Statement statement,
                                                 FinishRegistrationRequestDto finishRegistrationRequestDto) {
        log.info(
                "FinishRegistrationServiceImpl заполнение ScoringDataDto из Statement: {} и FinishRegistrationRequestDto: {}",
                statement, finishRegistrationRequestDto);
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .firstName(statement.getClient().getFirstName())
                .lastName(statement.getClient().getLastName())
                .middleName(statement.getClient().getMiddleName())
                .gender(finishRegistrationRequestDto.getGender())
                .birthdate(statement.getClient().getBirthdate())
                .passportSeries(statement.getClient().getPassport().getSeries())
                .passportNumber(statement.getClient().getPassport().getNumber())
                .passportIssueDate(finishRegistrationRequestDto.getPassportIssueDate())
                .passportIssueBranch(finishRegistrationRequestDto.getPassportIssueBranch())
                .maritalStatus(finishRegistrationRequestDto.getMaritalStatus())
                .dependentAmount(finishRegistrationRequestDto.getDependentAmount())
                .employment(finishRegistrationRequestDto.getEmployment())
                .accountNumber(finishRegistrationRequestDto.getAccountNumber())
                .build();
        log.info("FinishRegistrationServiceImpl результат заполнения: {}", scoringDataDto);
        return scoringDataDto;
    }

    private Credit createCreditEntity(CreditDto creditDto) {
        log.info("FinishRegistrationServiceImpl создание Credit на основе CreditDto: {}", creditDto);
        Credit credit = Credit.builder()
                .psk(creditDto.getPsk())
                .creditStatus(CreditStatus.CALCULATED)
                .amount(creditDto.getAmount())
                .rate(creditDto.getRate())
                .term(creditDto.getTerm())
                .monthlyPayment(creditDto.getMonthlyPayment())
                .salaryClient(creditDto.getIsSalaryClient())
                .insuranceEnabled(creditDto.getIsInsuranceEnabled())
                .build();
        log.info("FinishRegistrationServiceImpl созданный Credit: {}", credit);
        return credit;
    }

}
