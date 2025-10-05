package ru.realtorcrm.core_crm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.realtorcrm.core_crm.dto.BuildingRequestDto;
import ru.realtorcrm.core_crm.entity.Building;
import ru.realtorcrm.core_crm.service.BuildingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/buildings")
@AllArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;

    @GetMapping
    public List<Building> getAllBuildings() {
        return buildingService.getAllBuildings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBuildingById(@PathVariable Long id) {
        try {
            Building building = buildingService.getBuildingById(id);
            return ResponseEntity.ok(building);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> createBuilding(@RequestBody BuildingRequestDto dto) {
        try {
            Building building = buildingService.createBuilding(dto);
            log.info("Building with id {} was added", building.getId() );
            return ResponseEntity.ok(building);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Building> updateBuilding(@PathVariable Long id,
                                                   @RequestBody BuildingRequestDto dto) {
        try {
            Building updateBuilding = buildingService.updateBuilding(dto, id);
            log.info("Building with id {} was updated", updateBuilding.getId() );
            return ResponseEntity.ok(updateBuilding);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        try {
            buildingService.deleteBuilding(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
