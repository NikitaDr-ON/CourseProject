package com.neoflex.deal.service;

import com.neoflex.deal.dto.EmailMessage;
import com.neoflex.deal.entity.Statement;
import com.neoflex.deal.enums.Theme;
import com.neoflex.deal.exception.NotFoundException;
import com.neoflex.deal.model.EmailMessageProperties;
import com.neoflex.deal.repository.StatementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class FillEmailMessageDtoServiceImpl implements FillEmailMessageDtoService {

    private final StatementRepository statementRepository;
    private final EmailMessageProperties emailMessageProperties;

    @Autowired
    public FillEmailMessageDtoServiceImpl(StatementRepository statementRepository,
                                          EmailMessageProperties emailMessageProperties) {
        this.statementRepository = statementRepository;
        this.emailMessageProperties = emailMessageProperties;
    }

    @Override
    public EmailMessage fillEmailMessageDto(UUID statementId, Theme theme) {
        log.info("FillEmailMessageDtoServiceImpl fillEmailMessageDto старт заполнения полей emailMessageDto");
        Statement statement = statementRepository.findById(statementId)
                .orElseThrow(() -> new NotFoundException("заявка не была найдена"));
        EmailMessage emailMessage = EmailMessage.builder()
                .address(statement.getClient().getEmail())
                .statementId(statementId)
                .theme(theme)
                .build();
        setTextToEmailMessage(emailMessage);
        log.info("FillEmailMessageDtoServiceImpl fillEmailMessageDto результат заполнения полей emailMessageDto: {}",
                emailMessage);
        return emailMessage;
    }

    private void setTextToEmailMessage(EmailMessage emailMessage) {
        log.info("FillEmailMessageDtoServiceImpl setTextToEmailMessage заполнения текста письма");
        Random random = new Random();
        switch (emailMessage.getTheme()) {
            case FINISH_REGISTRATION -> emailMessage.setText("Завершите регистрацию");
            case CREATE_DOCUMENTS -> emailMessage.setText("Документы созданы");
            case SEND_DOCUMENTS -> emailMessage.setText("Документы отправлены");
            case SEND_SES -> emailMessage.setText(String.valueOf(random.nextInt(
                    emailMessageProperties.getMaxRangeForRandom() -
                            emailMessageProperties.getMinRangeForRandom()) +
                    emailMessageProperties.getMaxRangeForRandom()));
            case CREDIT_ISSUED -> emailMessage.setText("Кредит выдан");
            case STATEMENT_DENIED -> emailMessage.setText("отказ выдачи кредита");
        }
    }

}
