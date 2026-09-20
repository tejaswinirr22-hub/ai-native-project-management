package com.example.ainativeprojectmanagement.service;

import com.example.ainativeprojectmanagement.model.Agent;
import com.example.ainativeprojectmanagement.model.Task;
import com.example.ainativeprojectmanagement.repository.AgentRepository;
import com.example.ainativeprojectmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskCompletionService {

    private final TaskRepository taskRepository;
    private final AgentRepository agentRepository;
    private final AuditLogService auditLogService;
    private final ExecutionAttemptService executionAttemptService;

    public TaskCompletionService(
            TaskRepository taskRepository,
            AgentRepository agentRepository,
            AuditLogService auditLogService,
            ExecutionAttemptService executionAttemptService) {

        this.taskRepository = taskRepository;
        this.agentRepository = agentRepository;
        this.auditLogService = auditLogService;
        this.executionAttemptService = executionAttemptService;
    }

    public String completeTask(Long agentId, Long taskId) {

        Agent agent = agentRepository.findById(agentId).orElse(null);

        if (agent == null) {
            return "Agent not found";
        }

        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            return "Task not found";
        }

        if (!"RUNNING".equalsIgnoreCase(task.getStatus())) {
            return "Task cannot be completed because its status is "
                    + task.getStatus();
        }

        if (agent.getCurrentTaskId() == null
                || !agent.getCurrentTaskId().equals(taskId)) {
            return "This agent is not assigned to this task";
        }

        task.setStatus("COMPLETED");

        agent.setCurrentTaskId(null);
        agent.setStatus("IDLE");

        taskRepository.save(task);
        agentRepository.save(agent);

        /*
         * Automatically mark the latest execution attempt
         * as COMPLETED.
         */
        executionAttemptService.updateLatestAttempt(
                taskId,
                "COMPLETED",
                null,
                null,
                null,
                "Task successfully completed"
        );

        // Create audit record
        auditLogService.createLog(
                taskId,
                agentId,
                "TASK_COMPLETED",
                "COMPLETED",
                "Task successfully completed by agent"
        );

        return "Task " + taskId
                + " successfully completed by Agent " + agentId;
    }
}