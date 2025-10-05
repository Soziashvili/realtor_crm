package ru.realtorcrm.sending_notification_processor.service;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import ru.realtorcrm.sending_notification_processor.config.MailConfigProperties;

import java.util.Properties;

@Slf4j
@Service
@AllArgsConstructor
public class EmailService {

    private final MailConfigProperties mailProperties;

    @Retryable(
            retryFor = SendFailedException.class,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void sendMail(String text, String subject) throws SendFailedException {
        Properties properties = getProperties();

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailProperties.getEmailFrom(), mailProperties.getPassword());
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(mailProperties.getEmailFrom()));
            //message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailProperties.getEmailTo()));
            message.setRecipients(Message.RecipientType.TO, getRecipients());
            message.setSubject(subject);
            message.setContent(text, "text/html; charset=utf-8");

            Transport.send(message);

            log.info("Email Sent successfully...");
        } catch (Exception e){
            log.error("Email sent error: ", e);
            throw new SendFailedException(e.getMessage());
        }

    }

    private Properties getProperties() {
        Properties properties = new Properties();

        properties.put("mail.smtp.host", mailProperties.getHostName());
        properties.put("mail.smtp.port", mailProperties.getPort());
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        return properties;
    }

    private InternetAddress[] getRecipients() {
        return mailProperties.getEmailTo().stream().map(address -> {
            try {
                return new InternetAddress(address);
            } catch (AddressException e) {
                throw new RuntimeException(e);
            }
        }).toArray(InternetAddress[]::new);
    }
}
