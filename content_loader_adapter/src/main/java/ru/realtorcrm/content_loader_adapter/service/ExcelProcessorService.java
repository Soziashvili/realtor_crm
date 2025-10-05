package ru.realtorcrm.content_loader_adapter.service;

import ru.realtorcrm.content_loader_adapter.entity.Building;
import ru.realtorcrm.content_loader_adapter.entity.BuildingData;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ExcelProcessorService {

    public BuildingData processExcelFile(String filePath, String source) throws IOException {
        log.info("Processing Excel file: {} for source: {}", filePath, source);

        List<Building> buildings = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Building building = parseRow(row, source);
                    if (building != null) {
                        buildings.add(building);
                    }
                }
            }
        }

        BuildingData buildingData = new BuildingData();
        buildingData.setFileName(filePath);
        buildingData.setSourceSystem(source);
        buildingData.setBuildings(buildings);
        buildingData.setTotalRecords(buildings.size());
        buildingData.setProcessedAt(LocalDateTime.now());

        log.info("Processed {} buildings from file: {}", buildings.size(), filePath);
        return buildingData;
    }

    private Building parseRow(Row row, String source) {
        try {
            Building building = new Building();
            building.setSource(source);
            building.setLoadTimestamp(LocalDateTime.now());

            return switch (source.toUpperCase()) {
                case "CIAN" -> parseCianRow(row, building);
                case "ROSREESTR" -> parseRosreestrRow(row, building);
                case "DOMCLICK" -> parseDomclickRow(row, building);
                default -> {
                    log.warn("Unknown source: {}", source);
                    yield null;
                }
            };
        } catch (Exception e) {
            log.error("Error parsing row {}: {}", row.getRowNum(), e.getMessage());
            return null;
        }
    }

    private Building parseCianRow(Row row, Building building) {
        building.setId(getLongCellValue(row, 0));
        building.setCadastre((getStringCellValue(row, 1)));
        building.setType((getStringCellValue(row, 2)));
        building.setAddress(getStringCellValue(row, 3));
        building.setSquare(getBigDecimalCellValue(row, 4));
        building.setFloors(getIntegerCellValue(row, 5));
        building.setYearBuilt(getIntegerCellValue(row, 6));
        building.setMaterial(getStringCellValue(row, 7));
        building.setPrice(getBigDecimalCellValue(row, 8));
        building.setDistrict(getStringCellValue(row, 9));
        building.setStatus(getStringCellValue(row, 10));
        return building;
    }

    private Building parseRosreestrRow(Row row, Building building) {
        building.setId(getLongCellValue(row, 0));
        building.setCadastre((getStringCellValue(row, 1)));
        building.setType((getStringCellValue(row, 2)));
        building.setAddress(getStringCellValue(row, 3));
        building.setSquare(getBigDecimalCellValue(row, 4));
        building.setPrice(getBigDecimalCellValue(row, 5));
        building.setFloors(getIntegerCellValue(row, 6));
        building.setYearBuilt(getIntegerCellValue(row, 7));
        building.setMaterial(getStringCellValue(row, 8));
        building.setStatus(getStringCellValue(row, 9));
        return building;
    }

    private Building parseDomclickRow(Row row, Building building) {
        building.setId(getLongCellValue(row, 0));
        building.setCadastre((getStringCellValue(row, 1)));
        building.setType((getStringCellValue(row, 2)));
        building.setAddress(getStringCellValue(row, 3));
        building.setSquare(getBigDecimalCellValue(row, 4));
        building.setFloors(getIntegerCellValue(row, 5));
        building.setPrice(getBigDecimalCellValue(row, 6));
        building.setDistrict(getStringCellValue(row, 7));
        building.setStatus(getStringCellValue(row, 8));
        return building;
    }

    private String getStringCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return null;
        }
    }

    private Integer getIntegerCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }

    private Long getLongCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case NUMERIC:
                return (long) cell.getNumericCellValue();
            case STRING:
                try {
                    return Long.parseLong(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }

    private BigDecimal getBigDecimalCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING:
                try {
                    return new BigDecimal(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }
}
