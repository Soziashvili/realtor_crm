package ru.realtorcrm.sending_notification_processor.entity;

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
    private String source;
    private String address;
    private BigDecimal square;
    private Integer floors;
    private Integer yearBuilt;
    private String material;
    private BigDecimal price;
    private String district;
    private String status;
    private LocalDateTime loadTimestamp;

    @Override
    public String toString() {
        return "<tr>" +
                "<td>" + id + "</td>" +
                "<td>" + cadastre + "</td>" +
                "<td>" + type + "</td>" +
                "<td>" + source + "</td>" +
                "<td>" + address + "</td>" +
                "<td>" + square + "</td>" +
                "<td>" + floors + "</td>" +
                "<td>" + yearBuilt + "</td>" +
                "<td>" + material + "</td>" +
                "<td>" + price + "</td>" +
                "<td>" + district + "</td>" +
                "<td>" + status + "</td>" +
                "<td>" + loadTimestamp + "</td>" +
                "</tr>";
    }
}
