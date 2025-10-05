package ru.realtorcrm.core_crm.config;

import lombok.AllArgsConstructor;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import ru.realtorcrm.core_crm.dto.TaskDto;
import ru.realtorcrm.core_crm.entity.Building;
import ru.realtorcrm.core_crm.repository.BuildingRepository;
import ru.realtorcrm.core_crm.service.TaskService;

import java.time.LocalDate;
import java.util.List;

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@AllArgsConstructor
public class OwnerContactJob extends QuartzJobBean {

    private BuildingRepository buildingRepository;
    private TaskService taskService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        LocalDate thresholdDate = LocalDate.now().minusMonths(6);

        List<Building> buildingsToContact = buildingRepository
                .findByLastContactDateBeforeOrLastContactDateIsNull(thresholdDate);

        for (Building building : buildingsToContact) {

            TaskDto task = TaskDto.builder()
                    .title("Контакт владельца: " + building.getOwnerName())
                    .description("Связаться с владельцем объекта " + building.getAddress() +
                        " для уточнения возможной продажи. Контакт: " + building.getOwnerContact())
                    .dueDate(LocalDate.now().plusDays(2))
                    .status("NEW")
                    .building(building)
                    .build();

            taskService.createTask(task);
            building.setLastContactDate(LocalDate.now());
            buildingRepository.save(building);
        }
    }
}
