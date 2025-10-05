package ru.realtorcrm.core_crm.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.realtorcrm.core_crm.dto.BuildingRequestDto;
import ru.realtorcrm.core_crm.entity.Building;
import ru.realtorcrm.core_crm.exception.BuildingAlreadyExistException;
import ru.realtorcrm.core_crm.repository.BuildingRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;

    @Override
    @Transactional
    public Building createBuilding(BuildingRequestDto buildingDto) {
        if (buildingRepository.isCadastreExists(buildingDto.getCadastre())) {
            throw new BuildingAlreadyExistException(buildingDto.getCadastre());
        }

        Building building = Building.createBuildingFromDto(buildingDto);

        return buildingRepository.save(building);
    }

    @Override
    @Transactional
    public Building getBuildingById(Long id) {
        return buildingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Building with id %s does not exist", id)
                ));
    }

    @Override
    @Transactional
    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    @Override
    @Transactional
    public Building updateBuilding(BuildingRequestDto buildingDto, Long id) {
        return buildingRepository.findById(id)
                .map(building -> {
                    building.setAddress(buildingDto.getAddress());
                    building.setCadastre(buildingDto.getCadastre());
                    building.setSquare(buildingDto.getSquare());
                    building.setDescription(buildingDto.getDescription());
                    building.setYearBuilt(buildingDto.getYearBuilt());
                    building.setDistrict(buildingDto.getDistrict());
                    building.setMaterial(buildingDto.getMaterial());
                    building.setStatus(buildingDto.getStatus());
                    building.setType(buildingDto.getType());
                    building.setPrice(buildingDto.getPrice());
                    building.setOwnerName(buildingDto.getOwnerName());
                    building.setOwnerContact(buildingDto.getOwnerContact());
                    return buildingRepository.save(building);
                })
                .orElseGet(() -> buildingRepository.save(Building.createBuildingFromDto(buildingDto)));
    }

    @Override
    @Transactional
    public void deleteBuilding(Long id) {
        if (buildingRepository.findById(id).isPresent()) {
            buildingRepository.deleteById(id);
        }
    }
}
