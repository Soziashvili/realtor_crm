package ru.realtorcrm.content_processor.dto;

import lombok.Builder;
import lombok.Data;
import ru.realtorcrm.content_processor.entity.Building;
import ru.realtorcrm.content_processor.enums.SourceType;

import java.math.BigDecimal;

@Data
@Builder
public class EstateDto {
    private String cadastre;
    private String type;
    private BigDecimal square;
    private BigDecimal price;
    private SourceType source;

    public static EstateDto fromBuilding(Building building) {
        return EstateDto.builder()
                .cadastre(building.getCadastre())
                .type(building.getType())
                .square(building.getSquare())
                .price(building.getPrice())
                .source(SourceType.valueOf(building.getSource()))
                .build();
    }
}
