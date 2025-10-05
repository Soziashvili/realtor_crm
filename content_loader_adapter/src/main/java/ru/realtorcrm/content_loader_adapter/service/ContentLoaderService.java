package ru.realtorcrm.content_loader_adapter.service;

import lombok.AllArgsConstructor;
import ru.realtorcrm.content_loader_adapter.entity.BuildingData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class ContentLoaderService {

    private final ExcelProcessorService excelProcessorService;
    private final KafkaProducerService kafkaProducerService;

    public void processFile(String filePath, String source) {
        try {
            BuildingData buildingData = excelProcessorService.processExcelFile(filePath, source);
            kafkaProducerService.sendBuildingData(buildingData);
            log.info("Successfully processed file: {}", filePath);
        } catch (IOException e) {
            log.error("Error processing file: {}", filePath, e);
            throw new RuntimeException("Failed to process file: " + filePath, e);
        }
    }
}
