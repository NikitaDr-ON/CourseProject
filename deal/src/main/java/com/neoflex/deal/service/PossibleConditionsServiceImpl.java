package com.neoflex.deal.service;

import com.neoflex.deal.dto.LoanOfferDto;
import com.neoflex.deal.dto.LoanStatementRequestDto;
import com.neoflex.deal.entity.Client;
import com.neoflex.deal.dto.Passport;
import com.neoflex.deal.entity.Statement;
import com.neoflex.deal.repository.ClientRepository;
import com.neoflex.deal.repository.StatementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PossibleConditionsServiceImpl implements PossibleConditionsService {

    private final ClientRepository clientRepository;
    private final StatementRepository statementRepository;
    private final PostServiceImpl postServiceImpl;

    @Autowired
    PossibleConditionsServiceImpl(ClientRepository clientRepository, StatementRepository statementRepository,
                                  PostServiceImpl postServiceImpl) {
        this.clientRepository = clientRepository;
        this.statementRepository = statementRepository;
        this.postServiceImpl = postServiceImpl;
    }

    @Override
    public List<LoanOfferDto> getPossibleConditions(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("PossibleConditionsServiceServiceImpl getListLoanOfferDto входные параметры: {}",
                loanStatementRequestDto);
        Client client = createClient(loanStatementRequestDto);
        Statement statement = saveClient(client);
        List<LoanOfferDto> loanOfferDtoList = postServiceImpl.postOffers(loanStatementRequestDto);
        setStatementIdToLoanOfferDto(loanOfferDtoList, statement.getStatementId());
        log.info("PossibleConditionsServiceServiceImpl getListLoanOfferDto лист LoanOfferDto: {}", loanOfferDtoList);
        return loanOfferDtoList;
    }

    private Client createClient(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("PossibleConditionsServiceServiceImpl создание Client на основе loanStatementRequestDto: {}",
                loanStatementRequestDto);
        Passport passport = fillPassportData(loanStatementRequestDto);
        Client client = Client.builder()
                .lastName(loanStatementRequestDto.getLastName())
                .firstName(loanStatementRequestDto.getFirstName())
                .middleName(loanStatementRequestDto.getMiddleName())
                .birthdate(loanStatementRequestDto.getBirthdate())
                .email(loanStatementRequestDto.getEmail())
                .passport(passport)
                .build();
        clientRepository.save(client);
        log.info("PossibleConditionsServiceServiceImpl сохраненный Client: {}", client);
        return client;
    }

    private Statement saveClient(Client client) {
        log.info("PossibleConditionsServiceServiceImpl создание Statement с связью на Client: {}", client);
        Statement statement = Statement.builder()
                .client(client)
                .build();
        statementRepository.save(statement);
        log.info("PossibleConditionsServiceServiceImpl сохраненный Statement: {}", statement);
        return statement;
    }

    private void setStatementIdToLoanOfferDto(List<LoanOfferDto> loanOfferDtoList, UUID statementId) {
        log.info("PossibleConditionsServiceServiceImpl присваивание листу LoanOfferDto: {}, id заявки: {}",
                loanOfferDtoList, statementId);
        for (LoanOfferDto loanOfferDto : loanOfferDtoList) {
            loanOfferDto.setStatementId(statementId);
        }
        log.info("PossibleConditionsServiceServiceImpl результат присваивание id для листа LoanOfferDto: {}",
                loanOfferDtoList);
    }

    private Passport fillPassportData(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("PossibleConditionsServiceServiceImpl заполнение Passport на основе LoanStatementRequestDto: {}",
                loanStatementRequestDto);
        Passport passport = Passport.builder()
                .series(loanStatementRequestDto.getPassportSeries())
                .number(loanStatementRequestDto.getPassportNumber())
                .build();
        log.info("PossibleConditionsServiceServiceImpl заполненный Passport: {}", passport);
        return passport;
    }

}
