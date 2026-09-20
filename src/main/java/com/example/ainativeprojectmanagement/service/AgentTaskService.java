package com.example.ainativeprojectmanagement.service;

import com.example.ainativeprojectmanagement.model.Agent;
import com.example.ainativeprojectmanagement.model.Task;
import com.example.ainativeprojectmanagement.repository.AgentRepository;
import com.example.ainativeprojectmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentTaskService {

    private final AgentRepository agentRepository;
    private final TaskRepository taskRepository;
    private final AuditLogService auditLogService;
    private final TaskReadinessService taskReadinessService;

    public AgentTaskService(
            AgentRepository agentRepository,
            TaskRepository taskRepository,
            AuditLogService auditLogService,
            TaskReadinessService taskReadinessService) {

        this.agentRepository = agentRepository;
        this.taskRepository = taskRepository;
        this.auditLogService = auditLogService;
        this.taskReadinessService = taskReadinessService;
    }

    public String claimTask(Long agentId, Long taskId) {

        Agent agent = agentRepository.findById(agentId).orElse(null);

        if (agent == null) {
            return "Agent not found";
        }

        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            return "Task not found";
        }

        List<String> readinessIssues =
                taskReadinessService.checkReadiness(taskId);

        if (!readinessIssues.isEmpty()) {
            return "Task is not ready for execution. Issues: "
                    + String.join("; ", readinessIssues);
        }

        if (!"PENDING".equalsIgnoreCase(task.getStatus())
                && !"RETRY".equalsIgnoreCase(task.getStatus())) {

            return "Task cannot be claimed because its status is "
                    + task.getStatus();
        }

        if (agent.getCurrentTaskId() != null) {
            return "Agent already has a task";
        }

        task.setStatus("CLAIMED");

        agent.setCurrentTaskId(taskId);
        agent.setStatus("BUSY");

        taskRepository.save(task);
        agentRepository.save(agent);

        auditLogService.createLog(
                taskId,
                agentId,
                "TASK_CLAIMED",
                "CLAIMED",
                "Agent successfully claimed the task"
        );

        return "Task " + taskId
                + " successfully claimed by Agent " + agentId;
    }

    public String releaseTask(Long agentId, Long taskId) {

        Agent agent = agentRepository.findById(agentId).orElse(null);

        if (agent == null) {
            return "Agent not found";
        }

        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            return "Task not found";
        }

        if (agent.getCurrentTaskId() == null
                || !agent.getCurrentTaskId().equals(taskId)) {

            return "This agent is not assigned to this task";
        }

        if (!"CLAIMED".equalsIgnoreCase(task.getStatus())) {

            return "Task cannot be released because its status is "
                    + task.getStatus();
        }

        task.setStatus("PENDING");

        agent.setCurrentTaskId(null);
        agent.setStatus("IDLE");

        taskRepository.save(task);
        agentRepository.save(agent);

        auditLogService.createLog(
                taskId,
                agentId,
                "TASK_RELEASED",
                "PENDING",
                "Agent released the task without executing it"
        );

        return "Task " + taskId
                + " released by Agent " + agentId;
    }
}