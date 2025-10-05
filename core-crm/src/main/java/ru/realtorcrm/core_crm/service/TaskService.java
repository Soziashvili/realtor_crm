package ru.realtorcrm.core_crm.service;

import ru.realtorcrm.core_crm.dto.TaskDto;
import ru.realtorcrm.core_crm.entity.Task;

import java.util.List;

public interface TaskService {
    Task createTask(TaskDto taskDto);
    Task getTask(Long id);
    List<Task> getAllTasks();
    Task updateTask(TaskDto taskDto, Long id);
    Task assignTaskToUser(Long taskId, Long userId, Long version);
}
