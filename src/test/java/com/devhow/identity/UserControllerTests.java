package com.devhow.identity;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"mail.test=true"})
public class UserControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void noLogin() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mvc.perform(get("/")).andExpect(status().isOk());
        mvc.perform(get("/public/sign-up")).andExpect(status().isOk());
        mvc.perform(get("/public/forgot-password")).andExpect(status().isOk());
    }

    JavaMailSender javaMailSender = new TestSender();

    @Service
    private class TestSender implements JavaMailSender {
        @Override
        public MimeMessage createMimeMessage() {
            return null;
        }

        @Override
        public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
            return null;
        }

        @Override
        public void send(MimeMessage mimeMessage) throws MailException {

        }

        @Override
        public void send(MimeMessage... mimeMessages) throws MailException {

        }

        @Override
        public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {

        }

        @Override
        public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {

        }

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {

        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {

        }
    }

    // @PostMapping("/public/do-sign-in")
    // Perform a sign in

    // @GetMapping("/public/sign-up")
    // Start a sign up

    // @PostMapping("/public/sign-up")
    // Perform the sign-up

    // @GetMapping("public/sign-up/confirm")
    // URL sent via email to confirm the new account

    // Step 1: View the password reset form
    // @GetMapping("/public/forgot-password")
    // Start a password reset flow - view the form

    // Step 2: The password reset form submitted, email sent
    // @PostMapping("/public/forgot-password")
    // Submit the password reset request

    // Step 3: The link clicked on in the password reset email
    // @GetMapping("/public/password-reset")
    // Start a password reset action

    // Step 4: The password reset form submitted
    // @PostMapping("/public/password-reset")
    // Actually perform the password reset


}
