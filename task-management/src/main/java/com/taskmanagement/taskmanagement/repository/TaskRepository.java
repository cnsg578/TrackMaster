package com.taskmanagement.taskmanagement.repository;

import com.taskmanagement.taskmanagement.entity.Task;
import com.taskmanagement.taskmanagement.entity.TaskPriority;
import com.taskmanagement.taskmanagement.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(TaskPriority priority);

    List<Task> findByAssignedToId(Long userId);

    List<Task> findByStatusAndPriority(
            TaskStatus status,
            TaskPriority priority
    );

    List<Task> findByTitleContainingIgnoreCase(String title);
}