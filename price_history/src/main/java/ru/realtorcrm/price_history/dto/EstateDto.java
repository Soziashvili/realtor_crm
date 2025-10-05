package ru.realtorcrm.price_history.dto;

import lombok.Builder;
import lombok.Data;
import ru.realtorcrm.price_history.entity.Building;
import ru.realtorcrm.price_history.enums.SourceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class EstateDto {
    private String cadastre;
    private String type;
    private BigDecimal square;
    private BigDecimal price;
    private SourceType source;
    private LocalDateTime loadDate;

    public static EstateDto fromBuilding(Building building) {
        return EstateDto.builder()
                .cadastre(building.getCadastre())
                .type(building.getType())
                .square(building.getSquare())
                .price(building.getPrice())
                .source(SourceType.valueOf(building.getSource()))
                .loadDate(building.getLoadTimestamp())
                .build();
    }
}
