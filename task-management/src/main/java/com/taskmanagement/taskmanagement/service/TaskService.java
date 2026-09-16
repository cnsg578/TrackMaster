package com.taskmanagement.taskmanagement.service;

import com.taskmanagement.taskmanagement.dto.TaskRequest;
import com.taskmanagement.taskmanagement.dto.TaskResponse;
import com.taskmanagement.taskmanagement.entity.Task;
import com.taskmanagement.taskmanagement.entity.TaskPriority;
import com.taskmanagement.taskmanagement.entity.TaskStatus;
import java.util.Comparator;

import java.util.List;
import java.util.stream.Collectors;
import com.taskmanagement.taskmanagement.entity.User;
import com.taskmanagement.taskmanagement.entity.Team;
import com.taskmanagement.taskmanagement.repository.TaskRepository;
import com.taskmanagement.taskmanagement.repository.TeamRepository;
import com.taskmanagement.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TaskService(
            TaskRepository taskRepository,
            TeamRepository teamRepository,
            UserRepository userRepository) {

        this.taskRepository = taskRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public TaskResponse createTask(
            TaskRequest request,
            String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() ->
                        new RuntimeException("Team not found"));

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(
                request.getPriority() != null
                        ? request.getPriority()
                        : TaskPriority.MEDIUM
        );
        task.setDueDate(request.getDueDate());
        task.setCreatedBy(user);
        task.setTeam(team);

        Task savedTask = taskRepository.save(task);

        return toTaskResponse(savedTask);
    }

    private TaskResponse toTaskResponse(Task task) {

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedBy().getId(),
                task.getAssignedTo() != null
                        ? task.getAssignedTo().getId()
                        : null,
                task.getTeam().getId(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
    public List<TaskResponse> getAllTasks(
        TaskStatus status,
        TaskPriority priority,
        Long assignedTo,
        String search,
        String sortBy,
        String direction) {

    List<Task> tasks;

    if (status != null && priority != null) {
        tasks = taskRepository.findByStatusAndPriority(
                status,
                priority
        );
    } else if (status != null) {
        tasks = taskRepository.findByStatus(status);
    } else if (priority != null) {
        tasks = taskRepository.findByPriority(priority);
    } else if (assignedTo != null) {
        tasks = taskRepository.findByAssignedToId(assignedTo);
    } else if (search != null && !search.isBlank()) {
        tasks = taskRepository.findByTitleContainingIgnoreCase(search);
    } else {
        tasks = taskRepository.findAll();
    }

    Comparator<Task> comparator = null;

    if ("dueDate".equalsIgnoreCase(sortBy)) {
        comparator = Comparator.comparing(
                Task::getDueDate,
                Comparator.nullsLast(Comparator.naturalOrder())
        );
    } else if ("createdAt".equalsIgnoreCase(sortBy)) {
        comparator = Comparator.comparing(Task::getCreatedAt);
    } else if ("updatedAt".equalsIgnoreCase(sortBy)) {
        comparator = Comparator.comparing(Task::getUpdatedAt);
    }

    if (comparator != null) {

        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }

        tasks.sort(comparator);
    }

    return tasks.stream()
            .map(this::toTaskResponse)
            .collect(Collectors.toList());
}
public TaskResponse getTaskById(Long taskId) {

    Task task = taskRepository.findById(taskId)
            .orElseThrow(() ->
                    new RuntimeException("Task not found"));

    return toTaskResponse(task);
}
public TaskResponse updateTask(Long taskId, TaskRequest request) {

    Task task = taskRepository.findById(taskId)
            .orElseThrow(() ->
                    new RuntimeException("Task not found"));

    Team team = teamRepository.findById(request.getTeamId())
            .orElseThrow(() ->
                    new RuntimeException("Team not found"));

    task.setTitle(request.getTitle());
    task.setDescription(request.getDescription());
    task.setPriority(
            request.getPriority() != null
                    ? request.getPriority()
                    : TaskPriority.MEDIUM
    );
    task.setDueDate(request.getDueDate());
    task.setTeam(team);

    Task updatedTask = taskRepository.save(task);

    return toTaskResponse(updatedTask);
}
public void deleteTask(Long taskId) {

    if (!taskRepository.existsById(taskId)) {
        throw new RuntimeException("Task not found");
    }

    taskRepository.deleteById(taskId);
}
public TaskResponse assignTask(Long taskId, Long userId) {

    Task task = taskRepository.findById(taskId)
            .orElseThrow(() ->
                    new RuntimeException("Task not found"));

    User user = userRepository.findById(userId)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    task.setAssignedTo(user);

    Task updatedTask = taskRepository.save(task);

    return toTaskResponse(updatedTask);
}
public TaskResponse updateTaskStatus(Long taskId, TaskStatus status) {

    Task task = taskRepository.findById(taskId)
            .orElseThrow(() ->
                    new RuntimeException("Task not found"));

    task.setStatus(status);

    Task updatedTask = taskRepository.save(task);

    return toTaskResponse(updatedTask);
}
public TaskResponse updateTaskPriority(Long taskId, TaskPriority priority) {

    Task task = taskRepository.findById(taskId)
            .orElseThrow(() ->
                    new RuntimeException("Task not found"));

    task.setPriority(priority);

    Task updatedTask = taskRepository.save(task);

    return toTaskResponse(updatedTask);
}
}