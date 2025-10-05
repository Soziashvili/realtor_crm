package ru.realtorcrm.content_loader_adapter.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Building {
    private Long id;
    private String cadastre;
    private String type;
    private String source; // CIAN, ROSREESTR, DOMCLICK
    private String address;
    private BigDecimal square;
    private Integer floors;
    private Integer yearBuilt;
    private String material;
    private BigDecimal price;
    private String district;
    private String status;
    private LocalDateTime loadTimestamp;
}
