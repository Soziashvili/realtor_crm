package ru.realtorcrm.core_crm.dto;

import lombok.Builder;
import lombok.Data;
import ru.realtorcrm.core_crm.entity.Building;
import ru.realtorcrm.core_crm.entity.User;

import java.time.LocalDate;

@Data
@Builder
public class TaskDto {
    private String title;
    private String description;
    private LocalDate dueDate;
    private Building building;
    private User assignedUser;
    private String status;
}
