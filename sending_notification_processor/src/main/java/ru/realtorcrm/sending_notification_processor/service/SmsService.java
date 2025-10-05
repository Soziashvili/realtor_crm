package ru.realtorcrm.sending_notification_processor.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import ru.realtorcrm.sending_notification_processor.config.SmsConfigProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@AllArgsConstructor
public class SmsService {

    private final SmsConfigProperties smsProperties;

    @Retryable(
            retryFor = IOException.class,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void sendSms(String text) throws IOException {
        try {
            String URL_STRING = String.format(
                    smsProperties.getProviderUrl(),
                    smsProperties.getApiId(),
                    smsProperties.getPhone(),
                    URLEncoder.encode(text, StandardCharsets.UTF_8),
                    smsProperties.getSender()
            );
            URL url = new URL(URL_STRING);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            log.info("Server response: {}", response);
        } catch (IOException e) {
            log.error("Sms service error: ", e);
            throw new IOException(e);
        }
    }
}
