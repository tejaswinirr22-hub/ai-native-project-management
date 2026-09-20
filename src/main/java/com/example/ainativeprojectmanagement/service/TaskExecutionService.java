package com.example.ainativeprojectmanagement.service;

import com.example.ainativeprojectmanagement.model.Agent;
import com.example.ainativeprojectmanagement.model.Task;
import com.example.ainativeprojectmanagement.repository.AgentRepository;
import com.example.ainativeprojectmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskExecutionService {

    private final TaskRepository taskRepository;
    private final AgentRepository agentRepository;
    private final AuditLogService auditLogService;
    private final ExecutionAttemptService executionAttemptService;

    public TaskExecutionService(
            TaskRepository taskRepository,
            AgentRepository agentRepository,
            AuditLogService auditLogService,
            ExecutionAttemptService executionAttemptService) {

        this.taskRepository = taskRepository;
        this.agentRepository = agentRepository;
        this.auditLogService = auditLogService;
        this.executionAttemptService = executionAttemptService;
    }

    public String startTask(Long taskId, Long agentId) {

        Agent agent = agentRepository.findById(agentId).orElse(null);

        if (agent == null) {
            return "Agent not found";
        }

        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            return "Task not found";
        }

        if (!"CLAIMED".equalsIgnoreCase(task.getStatus())) {
            return "Task cannot be started because its status is "
                    + task.getStatus();
        }

        if (agent.getCurrentTaskId() == null
                || !agent.getCurrentTaskId().equals(taskId)) {

            return "Agent " + agentId
                    + " has not claimed Task " + taskId;
        }

        task.setStatus("RUNNING");

        taskRepository.save(task);

        int attemptNumber = (task.getRetryCount() == null)
                ? 1
                : task.getRetryCount() + 1;

        executionAttemptService.createAttempt(
                taskId,
                agentId,
                attemptNumber
        );

        auditLogService.createLog(
                taskId,
                agentId,
                "TASK_STARTED",
                "RUNNING",
                "Task execution started by agent"
        );

        return "Task " + taskId
                + " execution started by Agent " + agentId;
    }
}