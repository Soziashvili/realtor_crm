package ru.realtorcrm.sending_notification_processor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mail")
public class MailConfigProperties {
    String hostName;
    int port;
    String emailFrom;
    List<String> emailTo;
    String password;
}
