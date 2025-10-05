package ru.realtorcrm.sending_notification_processor.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.realtorcrm.sending_notification_processor.entity.Building;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class SendingService {

    private final EmailService emailService;
    private final SmsService smsService;

    private static final int BATCH_SIZE = 1000;
    private List<String> buildings;

    public synchronized void processBuilding(Building building) {
        try {
            buildings.add(String.valueOf(building));

            if (buildings.size() >= BATCH_SIZE) {
                flushBatch();
            }
        } catch (Exception e) {
            log.error("Error processing building: {}", building, e);
        }
    }

    public synchronized void flushBatch() {
        if (buildings.isEmpty()) return;

        try {
            int batchSize = buildings.size();
            String text = "<table border=\"1\" style=\"border-collapse: collapse\"" +
                    "<tr><th>id</th><th>cadastre</th><th>type</th></tr><th>source</th><th>address</th><th>square</th>" +
                    "<th>floors</th><th>yearBuilt</th><th>material</th><th>price</th><th>district</th><th>status</th>" +
                    "<th>loadTimestamp</th></tr>" + String.join("", buildings) +
                    "</table>";
            String subject =  batchSize + " new real estate properties were received | " + LocalDateTime.now();

            emailService.sendMail(text, subject);
            smsService.sendSms(subject);

            log.info("Processed batch of {} estates", batchSize);
        } catch (Exception e) {
            log.error("Error flushing batch", e);
        } finally {
            buildings.clear();
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void scheduledFlush() {
        flushBatch();
    }

}
