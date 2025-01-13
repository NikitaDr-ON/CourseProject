package com.neoflex.deal.service;

import com.neoflex.deal.dto.AppliedOffer;
import com.neoflex.deal.dto.LoanOfferDto;
import com.neoflex.deal.dto.StatusHistory;
import com.neoflex.deal.entity.Statement;
import com.neoflex.deal.enums.ApplicationStatus;
import com.neoflex.deal.enums.ChangeType;
import com.neoflex.deal.exception.NotFoundException;
import com.neoflex.deal.repository.StatementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class SelectServiceImpl implements SelectService {

    private final StatementRepository statementRepository;

    @Autowired
    public SelectServiceImpl(StatementRepository statementRepository) {
        this.statementRepository = statementRepository;
    }

    public void selectStatement(LoanOfferDto loanOfferDto) {
        log.info("SelectServiceImpl старт выбора заявки");
        Statement statement = getStatement(loanOfferDto);
        updateStatement(statement, loanOfferDto);
        statementRepository.save(statement);
        log.info("SelectServiceImpl выбрана и сохранена заявка: {}", statement);
    }

    private Statement getStatement(LoanOfferDto loanOfferDto) {
        log.info("SelectServiceImpl получение заявки по id из LoanOfferDto: {}", loanOfferDto);
        Statement statement = statementRepository.findById(loanOfferDto.getStatementId())
                .orElseThrow(() -> {
                    log.info("SelectServiceImpl заявка с id: {} не была найдена", loanOfferDto.getStatementId());
                    return new NotFoundException("заявка не найдена");
                });
        log.info("SelectServiceImpl найдена заявка: {}", statement);
        return statement;
    }

    private StatusHistory createNewElementOfStatusHistory(ApplicationStatus status) {
        log.info("SelectServiceImpl создание нового элемента StatusHistory на основе ApplicationStatus: {}", status);
        StatusHistory statusHistory = StatusHistory.builder()
                .status(String.valueOf(status))
                .time(LocalDateTime.from(LocalDateTime.now()))
                .changeType(ChangeType.AUTOMATIC)
                .build();
        log.info("SelectServiceImpl созданный элемент StatusHistory: {}", statusHistory);
        return statusHistory;
    }

    private AppliedOffer createAppliedOffer(Statement statement, LoanOfferDto loanOfferDto) {
        log.info("SelectServiceImpl создание AppliedOffer на основе Statement: {}, LoanOfferDto: {}", statement,
                loanOfferDto);
        AppliedOffer appliedOffer = AppliedOffer.builder()
                .appliedLoanOfferDto(loanOfferDto)
                .build();
        statement.setAppliedOffer(appliedOffer);
        log.info("SelectServiceImpl созданный AppliedOffer: {}", appliedOffer);
        return appliedOffer;
    }

    private void updateStatement(Statement statement, LoanOfferDto loanOfferDto) {
        log.info("SelectServiceImpl обновление заявки");
        statement.setStatus(ApplicationStatus.PREAPPROVAL);
        List<StatusHistory> statusHistories = statement.getStatusHistory();
        statusHistories.add(createNewElementOfStatusHistory(statement.getStatus()));
        statement.setStatusHistory(statusHistories);
        statement.setAppliedOffer(createAppliedOffer(statement, loanOfferDto));
        statementRepository.save(statement);
        log.info("в заявке обновлены AppliedOffer: {}, status: {}, StatusHistory: {}", statement.getAppliedOffer(),
                statement.getStatus(), statement.getStatusHistory());
    }

}
