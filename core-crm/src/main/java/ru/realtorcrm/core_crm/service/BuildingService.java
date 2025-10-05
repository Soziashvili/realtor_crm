package ru.realtorcrm.core_crm.service;

import ru.realtorcrm.core_crm.dto.BuildingRequestDto;
import ru.realtorcrm.core_crm.entity.Building;

import java.util.List;

public interface BuildingService {
    Building createBuilding(BuildingRequestDto building);
    Building getBuildingById(Long id);
    List<Building> getAllBuildings();
    Building updateBuilding(BuildingRequestDto building, Long id);
    void deleteBuilding(Long id);
}
