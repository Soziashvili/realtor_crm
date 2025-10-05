package ru.realtorcrm.sending_notification_processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class SendingNotificationProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendingNotificationProcessorApplication.class, args);
	}

}
