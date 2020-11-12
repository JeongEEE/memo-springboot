package com.example.Memo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Component
public class MailComponent {
    @Value("${mail.smtp.host}")
    private String host;
    @Value("${mail.username}")
    private String userName;
    @Value("${mail.password}")
    private String password;

    public boolean sendEmail(String email, String title, String content) {
        log.info("MailComponent sendEmail invoke. email:" + email + ", title:" + title + ", content:" + content);
        try {
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", host);
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.smtp.socketFactory.fallback", "false");
            properties.setProperty("mail.smtp.port", "465");
            properties.setProperty("mail.smtp.socketFactory.port", "465");
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userName));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(title);
            message.setText(content);
            Transport.send(message);
            log.info("MailComponent sendEmail return successfully");
            return true;
        } catch (MessagingException mex) {
            log.error("MailComponent sendEmail MessagingException:" + mex.getMessage());
            return false;
        }
    }

}

