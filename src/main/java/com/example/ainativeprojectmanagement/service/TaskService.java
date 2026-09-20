package com.example.ainativeprojectmanagement.service;

import com.example.ainativeprojectmanagement.model.Task;
import com.example.ainativeprojectmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final AuditLogService auditLogService;

    public TaskService(
            TaskRepository taskRepository,
            AuditLogService auditLogService) {

        this.taskRepository = taskRepository;
        this.auditLogService = auditLogService;
    }

    public Task createTask(Task task) {

        if (task.getStatus() == null) {
            task.setStatus("PENDING");
        }

        if (task.getRetryCount() == null) {
            task.setRetryCount(0);
        }

        if (task.getMaxRetries() == null) {
            task.setMaxRetries(3);
        }

        Task savedTask = taskRepository.save(task);

        auditLogService.createLog(
                savedTask.getId(),
                null,
                "TASK_CREATED",
                savedTask.getStatus(),
                "Task created successfully"
        );

        return savedTask;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(Long id, Task updatedTask) {

        Optional<Task> existingTask = taskRepository.findById(id);

        if (existingTask.isEmpty()) {
            return null;
        }

        Task task = existingTask.get();

        task.setTitle(updatedTask.getTitle());
        task.setObjective(updatedTask.getObjective());
        task.setRepository(updatedTask.getRepository());
        task.setServiceName(updatedTask.getServiceName());
        task.setStatus(updatedTask.getStatus());
        task.setAcceptanceCriteria(updatedTask.getAcceptanceCriteria());
        task.setDependencies(updatedTask.getDependencies());

        Task savedTask = taskRepository.save(task);

        auditLogService.createLog(
                savedTask.getId(),
                null,
                "TASK_UPDATED",
                savedTask.getStatus(),
                "Task updated successfully"
        );

        return savedTask;
    }

    public boolean deleteTask(Long id) {

        if (!taskRepository.existsById(id)) {
            return false;
        }

        taskRepository.deleteById(id);

        auditLogService.createLog(
                id,
                null,
                "TASK_DELETED",
                "DELETED",
                "Task deleted successfully"
        );

        return true;
    }
}