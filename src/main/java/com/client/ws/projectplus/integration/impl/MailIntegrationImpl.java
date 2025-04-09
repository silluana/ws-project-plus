package com.client.ws.projectplus.integration.impl;

import com.client.ws.projectplus.integration.MailIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailIntegrationImpl implements MailIntegration {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void send(String mailTo, String subject, String messege) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mailTo);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(messege);

        javaMailSender.send(simpleMailMessage);
    }
}
