package com.beinghealer.service;

import com.beinghealer.model.Email;
import com.beinghealer.repository.EmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by SBI on 4/2/2018.
 */
@Component
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailRepository repo;

    public void send(Email eParams) {
        repo.save(eParams);
        try {
            if (eParams.isHtml()) {
                sendHtmlMail(eParams);
            } else {
                sendPlainTextMail(eParams);
            }
        } catch (MessagingException e) {
            logger.error("Could not send email to : {} Error = {}", eParams.getToAsList(), e.getMessage());
        }
    }

    private void sendHtmlMail(Email eParams) throws MessagingException {
        boolean isHtml = true;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(eParams.getTo().toArray(new String[eParams.getTo().size()]));
        helper.setReplyTo(eParams.getFrom());
        helper.setFrom(eParams.getFrom());
        helper.setSubject(eParams.getName());
        helper.setText(eParams.getMessage(), isHtml);
        mailSender.send(message);
    }

    private void sendPlainTextMail(Email eParams) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        eParams.getTo().toArray(new String[eParams.getTo().size()]);
        mailMessage.setTo(eParams.getTo().toArray(new String[eParams.getTo().size()]));
        mailMessage.setReplyTo(eParams.getFrom());
        mailMessage.setFrom(eParams.getFrom());
        mailMessage.setSubject(eParams.getName());
        mailMessage.setText(eParams.getMessage());
        mailSender.send(mailMessage);
    }
}