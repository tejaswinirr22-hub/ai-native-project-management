package com.example.ainativeprojectmanagement.service;

import com.example.ainativeprojectmanagement.model.Agent;
import com.example.ainativeprojectmanagement.model.Task;
import com.example.ainativeprojectmanagement.repository.AgentRepository;
import com.example.ainativeprojectmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskFailureService {

    private final TaskRepository taskRepository;
    private final AgentRepository agentRepository;
    private final AuditLogService auditLogService;
    private final ExecutionAttemptService executionAttemptService;

    public TaskFailureService(
            TaskRepository taskRepository,
            AgentRepository agentRepository,
            AuditLogService auditLogService,
            ExecutionAttemptService executionAttemptService) {

        this.taskRepository = taskRepository;
        this.agentRepository = agentRepository;
        this.auditLogService = auditLogService;
        this.executionAttemptService = executionAttemptService;
    }

    public String failTask(Long agentId, Long taskId, String errorMessage) {

        Agent agent = agentRepository.findById(agentId).orElse(null);

        if (agent == null) {
            return "Agent not found";
        }

        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            return "Task not found";
        }

        if (!"RUNNING".equalsIgnoreCase(task.getStatus())) {
            return "Task cannot be failed because its status is "
                    + task.getStatus();
        }

        if (agent.getCurrentTaskId() == null
                || !agent.getCurrentTaskId().equals(taskId)) {
            return "This agent is not assigned to this task";
        }

        int retryCount = task.getRetryCount() == null
                ? 0
                : task.getRetryCount();

        int maxRetries = task.getMaxRetries() == null
                ? 3
                : task.getMaxRetries();

        retryCount++;

        task.setRetryCount(retryCount);

        String newStatus;

        if (retryCount < maxRetries) {
            newStatus = "RETRY";
        } else {
            newStatus = "HUMAN_REVIEW";
        }

        task.setStatus(newStatus);

        agent.setCurrentTaskId(null);
        agent.setStatus("IDLE");

        taskRepository.save(task);
        agentRepository.save(agent);

        /*
         * Update the execution attempt with failure information.
         *
         * This preserves the context of what went wrong during
         * the agent's execution.
         */
        executionAttemptService.updateLatestAttempt(
                taskId,
                "FAILED",
                errorMessage,
                null,
                null,
                "Task execution failed"
        );

        // Create audit record
        auditLogService.createLog(
                taskId,
                agentId,
                "TASK_FAILED",
                newStatus,
                "Attempt " + retryCount
                        + " failed. Error: " + errorMessage
        );

        if (retryCount < maxRetries) {
            return "Task failed. Retry required. Error: "
                    + errorMessage;
        }

        return "Task failed after maximum retries. "
                + "Human review required. Error: "
                + errorMessage;
    }
}