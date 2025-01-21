package com.neoflex.dossier.kafka;

import com.neoflex.dossier.dto.EmailMessage;
import com.neoflex.dossier.email.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ListenerRequests {

    private final EmailSender emailSender;

    @Autowired
    public ListenerRequests(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    private static final String GROUP_ID = "documents";
    private static final String FINISH_REGISTRATION = "FINISH_REGISTRATION";
    private static final String CREATE_DOCUMENTS = "CREATE_DOCUMENTS";
    private static final String SEND_DOCUMENTS = "SEND_DOCUMENTS";
    private static final String SEND_SES = "SEND_SES";
    private static final String CREDIT_ISSUED = "CREDIT_ISSUED";
    private static final String STATEMENT_DENIED = "STATEMENT_DENIED";

    @KafkaListener(
            topics = FINISH_REGISTRATION,
            groupId = GROUP_ID)
    public void getRequest(EmailMessage emailMessage) {
        log.info("ListenerRequests getRequest получил следующее сообщение из kafka: {}", emailMessage);
        emailSender.sendMessage(emailMessage);
    }

}
