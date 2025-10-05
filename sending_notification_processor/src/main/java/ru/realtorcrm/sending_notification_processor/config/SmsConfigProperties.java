package ru.realtorcrm.sending_notification_processor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsConfigProperties {
    String apiId;
    String phone;
    String sender;
    String providerUrl;
}
