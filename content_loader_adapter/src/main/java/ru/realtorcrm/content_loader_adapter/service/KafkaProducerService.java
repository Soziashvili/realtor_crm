package ru.realtorcrm.content_loader_adapter.service;

import lombok.AllArgsConstructor;
import ru.realtorcrm.content_loader_adapter.entity.Building;
import ru.realtorcrm.content_loader_adapter.entity.BuildingData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@AllArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String BUILDINGS_TOPIC = "buildings-data";
    private static final int BATCH_SIZE = 1000;

    public void sendBuildingData(BuildingData buildingData) {
        List<Building> buildings = buildingData.getBuildings();

        for (int i = 0; i < buildings.size(); i += BATCH_SIZE) {
            List<Building> batch = buildings.subList(i, Math.min(i + BATCH_SIZE, buildings.size()));
            sendBatch(batch, buildingData.getSourceSystem());
        }

        log.info("Sent {} buildings to Kafka topic: {}", buildings.size(), BUILDINGS_TOPIC);
    }

    private void sendBatch(List<Building> batch, String source) {
        for (Building building : batch) {
            String key = generatePartitionKey(building, source);

            CompletableFuture<SendResult<String, Object>> future =
                     kafkaTemplate.send(BUILDINGS_TOPIC, key, building);

            future.thenAccept(System.out::println);
        }

        try {
            kafkaTemplate.flush();
        } catch (Exception e) {
            log.error("Error flushing batch", e);
        }
    }

    private String generatePartitionKey(Building building, String source) {
        String address = building.getAddress() != null ? building.getAddress() : "unknown";
        return String.valueOf(Math.abs(address.hashCode() % 10));
    }
}
