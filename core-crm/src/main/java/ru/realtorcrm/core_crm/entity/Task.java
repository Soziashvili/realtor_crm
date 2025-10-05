package ru.realtorcrm.core_crm.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.realtorcrm.core_crm.dto.TaskDto;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    private String title;
    private String description;

    @Column(nullable = false)
    private LocalDate dueDate;

    @ManyToOne
    private Building building;

    @ManyToOne
    private User assignedUser;
    private String status;

    @Version
    private Long version;

    public static Task createTaskFromDto(TaskDto taskDto) {
        return builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dueDate(taskDto.getDueDate())
                .building(taskDto.getBuilding())
                .assignedUser(taskDto.getAssignedUser())
                .status(taskDto.getStatus())
                .build();
    }
}
