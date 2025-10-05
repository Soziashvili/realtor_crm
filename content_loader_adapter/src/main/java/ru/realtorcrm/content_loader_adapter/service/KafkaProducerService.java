package ru.realtorcrm.content_loader_adapter.service;

import lombok.AllArgsConstructor;
import ru.realtorcrm.content_loader_adapter.entity.Building;
import ru.realtorcrm.content_loader_adapter.entity.BuildingData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
//            future.whenComplete((result, ex) -> {
//                if (ex == null) {
//                    System.out.println("Sent message=[" + source +
//                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
//                } else {
//                    System.out.println("Unable to send message=[" +
//                            source + "] due to : " + ex.getMessage());
//                }
//            });
        }

        // Ждем завершения отправки батча
        try {
            kafkaTemplate.flush();
        } catch (Exception e) {
            log.error("Error flushing batch", e);
        }
    }

    private String generatePartitionKey(Building building, String source) {
        // Генерация ключа для распределения по партициям
        // Можно использовать разные стратегии в зависимости от требований

        // Пример 1: По источнику данных
        // return source;

        // Пример 2: По району
        // return building.getDistrict() != null ? building.getDistrict() : "unknown";

        // Пример 3: Хэш от адреса
        String address = building.getAddress() != null ? building.getAddress() : "unknown";
        return String.valueOf(Math.abs(address.hashCode() % 10)); // 10 партиций
    }

    public void sendSync(Building building, String source) throws ExecutionException, InterruptedException {
        String key = generatePartitionKey(building, source);
        kafkaTemplate.send(BUILDINGS_TOPIC, key, building).get();
    }
}
