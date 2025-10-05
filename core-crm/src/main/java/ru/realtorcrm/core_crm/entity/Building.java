package ru.realtorcrm.core_crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.realtorcrm.core_crm.dto.BuildingRequestDto;
import ru.realtorcrm.core_crm.enums.BuildingType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Entity
@Table(name = "buildings")
@NoArgsConstructor
@AllArgsConstructor
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String address;

    @Column(nullable = false, unique = true)
    private String cadastre;

    @NotNull
    private BuildingType type;

    @NotNull
    private BigDecimal square;

    @NotNull
    private Integer yearBuilt;

    @NotNull
    private String ownerContact;

    private String ownerName;
    private String description;
    private String material;
    private String district;
    private BigDecimal price;
    private String status;
    private LocalDate lastContactDate;

    public static Building createBuildingFromDto(BuildingRequestDto buildingDto) {
        return builder()
                .address(buildingDto.getAddress())
                .cadastre(buildingDto.getCadastre())
                .square(buildingDto.getSquare())
                .description(buildingDto.getDescription())
                .yearBuilt(buildingDto.getYearBuilt())
                .type(buildingDto.getType())
                .material(buildingDto.getMaterial())
                .district(buildingDto.getDistrict())
                .price(buildingDto.getPrice())
                .ownerName(buildingDto.getOwnerName())
                .ownerContact(buildingDto.getOwnerContact())
                .status(buildingDto.getStatus())
                .build();
    }
}
