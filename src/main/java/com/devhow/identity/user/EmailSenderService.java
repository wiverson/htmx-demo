package com.devhow.identity.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

    private final JavaMailSender javaMailSender;

    @Value("${mail.test:false}")
    private final Boolean mailTest = false;

    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        if (mailTest) {
            logger.info(email.getText());
        } else {
            javaMailSender.send(email);
        }
    }
}