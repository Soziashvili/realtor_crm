package ru.realtorcrm.price_history.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
