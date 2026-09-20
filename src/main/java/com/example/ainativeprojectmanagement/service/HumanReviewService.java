package com.example.ainativeprojectmanagement.service;

import com.example.ainativeprojectmanagement.model.Task;
import com.example.ainativeprojectmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HumanReviewService {

    private final TaskRepository taskRepository;
    private final AuditLogService auditLogService;

    public HumanReviewService(
            TaskRepository taskRepository,
            AuditLogService auditLogService) {

        this.taskRepository = taskRepository;
        this.auditLogService = auditLogService;
    }

    public List<Task> getHumanReviewTasks() {

        return taskRepository.findAll()
                .stream()
                .filter(task ->
                        "HUMAN_REVIEW".equalsIgnoreCase(
                                task.getStatus()))
                .toList();
    }

    public String approveRetry(Long taskId) {

        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            return "Task not found";
        }

        if (!"HUMAN_REVIEW".equalsIgnoreCase(task.getStatus())) {
            return "Task is not waiting for human review";
        }

        task.setStatus("RETRY");

        taskRepository.save(task);

        auditLogService.createLog(
                taskId,
                null,
                "HUMAN_REVIEW_APPROVED",
                "RETRY",
                "Human approved another execution attempt"
        );

        return "Human review approved. Task " + taskId
                + " is available for retry";
    }
}