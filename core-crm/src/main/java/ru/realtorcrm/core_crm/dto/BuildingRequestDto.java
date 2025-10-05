package ru.realtorcrm.core_crm.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.realtorcrm.core_crm.enums.BuildingType;

import java.math.BigDecimal;

@Data
@Builder
public class BuildingRequestDto {
    @NotEmpty(message = "Адрес не млжет быть пустым")
    private String address;
    @NotEmpty(message = "Кадастровый номер не млжет быть пустым")
    private String cadastre;
    private BigDecimal square;
    private String description;
    private Integer yearBuilt;
    private String district;
    @NotNull(message = "Тип объекта обязателен")
    private BuildingType type;
    private String material;
    private BigDecimal price;
    private String ownerName;
    private String ownerContact;
    private String status;
}
