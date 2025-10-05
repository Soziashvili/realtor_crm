package ru.realtorcrm.content_processor.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.realtorcrm.content_processor.dto.EstateDto;
import ru.realtorcrm.content_processor.entity.Building;

import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class EstateProcessorService {

    private final JdbcTemplate jdbcTemplate;

    private static final int BATCH_SIZE = 1000;
    private List<Object[]> batchArgs;
    private static final String UPSERT_SQL =
            "INSERT INTO estates (cadastre, type, square, price, source) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT (cadastre, source) DO UPDATE SET " +
                    "type = EXCLUDED.type, " +
                    "square = EXCLUDED.square, " +
                    "price = EXCLUDED.price";

    public synchronized void processBuilding(Building building) {
        try {
            EstateDto dto = EstateDto.fromBuilding(building);
            Object[] params = new Object[]{
                    dto.getCadastre(),
                    dto.getType(),
                    dto.getSquare(),
                    dto.getPrice(),
                    dto.getSource().name()
            };

            batchArgs.add(params);

            if (batchArgs.size() >= BATCH_SIZE) {
                flushBatch();
            }
        } catch (Exception e) {
            log.error("Error processing building: {}", building, e);
        }
    }

    public synchronized void flushBatch() {
        if (batchArgs.isEmpty()) return;

        try {
            int[] updateCounts = jdbcTemplate.batchUpdate(UPSERT_SQL, batchArgs);
            log.info("Processed batch of {} estates", updateCounts.length);
        } catch (Exception e) {
            log.error("Error flushing batch", e);
        } finally {
            batchArgs.clear();
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void scheduledFlush() {
        flushBatch();
    }
}
