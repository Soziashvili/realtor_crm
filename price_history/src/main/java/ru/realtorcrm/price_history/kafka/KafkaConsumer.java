package ru.realtorcrm.price_history.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ru.realtorcrm.price_history.entity.Building;
import ru.realtorcrm.price_history.service.EstateProcessorService;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaConsumer {

    private final EstateProcessorService processorService;

    @KafkaListener(topics = "buildings-data",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeBatch(List<ConsumerRecord<String, Building>> records, Acknowledgment ack) {
        try {
            for (ConsumerRecord<String, Building> record : records) {
                Building building = record.value();
                processorService.processBuilding(building);
            }
            processorService.flushBatch();
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing batch", e);
        }
    }
}

