package com.neoflex.deal.service;

import com.neoflex.deal.dto.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageSenderServiceImpl implements MessageSenderService{

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

    @Autowired
    public MessageSenderServiceImpl(KafkaTemplate<String, EmailMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendRequests(EmailMessage emailMessage){
        log.info("kafka отправляет сообщение с EmailMessage: {}", emailMessage);
        kafkaTemplate.send(String.valueOf(emailMessage.getTheme()), emailMessage);
    }
}
