package com.neoflex.dossier.email;

import com.neoflex.dossier.dto.EmailMessage;
import com.neoflex.dossier.model.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    private final JavaMailSender javaMailSender;
    private final EmailProperties emailProperties;

    @Autowired
    public EmailSender(JavaMailSender javaMailSender, EmailProperties emailProperties) {
        this.javaMailSender = javaMailSender;
        this.emailProperties = emailProperties;
    }

    public void sendMessage(EmailMessage emailMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailProperties.getEmailUser());
        message.setTo(emailMessage.getAddress());
        message.setSubject(String.valueOf(emailMessage.getTheme()));
        message.setText(emailMessage.getText());
        javaMailSender.send(message);
    }

}
