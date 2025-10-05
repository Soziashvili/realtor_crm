package ru.realtorcrm.content_loader_adapter.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BuildingData {
    private String fileName;
    private String sourceSystem;
    private List<Building> buildings;
    private Integer totalRecords;
    private LocalDateTime processedAt;
}
