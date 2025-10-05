package ru.realtorcrm.core_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.realtorcrm.core_crm.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
