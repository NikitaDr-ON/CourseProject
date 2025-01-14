package com.neoflex.dossier.email;

import com.neoflex.dossier.model.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailSenderConfiguration {

    private final EmailProperties emailProperties;

    @Autowired
    public EmailSenderConfiguration(EmailProperties emailProperties){
        this.emailProperties=emailProperties;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailProperties.getEmailHost());
        mailSender.setPort(emailProperties.getEmailPort());
        mailSender.setUsername(emailProperties.getEmailUser());
        mailSender.setPassword(emailProperties.getEmailPassword());
        Properties props = mailSender.getJavaMailProperties();
        props.put(emailProperties.getEmailPropertyForProtocol(), emailProperties.getEmailProtocol());
        props.put(emailProperties.getEmailPropertyForAuth(), emailProperties.getIsAuthEnable());
        props.put(emailProperties.getEmailPropertyForStartTls(), emailProperties.getIsTlsEnable());
        props.put(emailProperties.getEmailPropertyForDebug(), emailProperties.getIsDebugEnable());
        return mailSender;
    }

}
