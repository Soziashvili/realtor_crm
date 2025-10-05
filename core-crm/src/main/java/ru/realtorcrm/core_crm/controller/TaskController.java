package ru.realtorcrm.core_crm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.realtorcrm.core_crm.dto.TaskDto;
import ru.realtorcrm.core_crm.entity.Task;
import ru.realtorcrm.core_crm.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskDto dto) {
        try {
            Task task = taskService.createTask(dto);
            return ResponseEntity.ok(task);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        try {
            Task task = taskService.getTask(id);
            return ResponseEntity.ok(task);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                           @RequestBody TaskDto taskDto) {
        try {
            Task task = taskService.updateTask(taskDto, id);
            return ResponseEntity.ok(task);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{taskId}/assign")
    public ResponseEntity<Task> assignTask(@PathVariable Long taskId,
                                           @RequestParam Long userId,
                                           @RequestParam Long version) {
        try {
            Task assignedTask = taskService.assignTaskToUser(taskId, userId, version);
            return ResponseEntity.ok(assignedTask);
        } catch (OptimisticLockingFailureException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }
}
