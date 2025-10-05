package ru.realtorcrm.core_crm.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import ru.realtorcrm.core_crm.dto.TaskDto;
import ru.realtorcrm.core_crm.entity.Task;
import ru.realtorcrm.core_crm.entity.User;
import ru.realtorcrm.core_crm.repository.TaskRepository;
import ru.realtorcrm.core_crm.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    private UserRepository userRepository;

    @Override
    @Transactional
    public Task createTask(TaskDto taskDto){
        Task newTask = Task.createTaskFromDto(taskDto);

        return taskRepository.save(newTask);
    }

    @Override
    @Transactional
    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    String.format("Task with id %s does not exist", id)
                ));
    }

    @Override
    @Transactional
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional
    public Task updateTask(TaskDto taskDto, Long id) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(taskDto.getTitle());
                    task.setDescription(taskDto.getDescription());
                    task.setDueDate(taskDto.getDueDate());
                    task.setBuilding(taskDto.getBuilding());
                    task.setAssignedUser(taskDto.getAssignedUser());
                    task.setStatus(taskDto.getStatus());
                    return taskRepository.save(task);
                }).orElseGet(() -> taskRepository.save(Task.createTaskFromDto(taskDto)));
    }

    @Override
    @Transactional
    public Task assignTaskToUser(Long taskId, Long userId, Long currentVersion) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!task.getVersion().equals(currentVersion)) {
            throw new OptimisticLockingFailureException("Task was modified by another transaction. Please refresh.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        task.setAssignedUser(user);
        task.setStatus("IN_PROGRESS");
        return taskRepository.save(task);
    }


}
