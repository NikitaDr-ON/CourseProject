package com.neoflex.dossier.email;

import com.neoflex.dossier.dto.EmailMessage;
import com.neoflex.dossier.enums.Theme;
import com.neoflex.dossier.model.EmailProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSenderTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private EmailProperties emailProperties;

    @InjectMocks
    private EmailSender emailSender;

    @BeforeEach
    void init() {
    }

    @Test
    void testSendMessage() {
        String to = "exampleMail";
        Theme subject = Theme.FINISH_REGISTRATION;
        String text = "Some text";
        String from = "myMail";

        EmailMessage emailMessage = EmailMessage.builder()
                .address(to)
                .text(text)
                .theme(subject)
                .build();

       when(emailProperties.getEmailUser()).thenReturn(from);

        emailSender.sendMessage(emailMessage);

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setFrom(from);
        expectedMessage.setTo(to);
        expectedMessage.setSubject(String.valueOf(subject));
        expectedMessage.setText(text);
        verify(javaMailSender, times(1)).send(expectedMessage);
    }
}