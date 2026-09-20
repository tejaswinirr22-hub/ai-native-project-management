package com.example.ainativeprojectmanagement.service;

import com.example.ainativeprojectmanagement.model.Task;
import com.example.ainativeprojectmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskReadinessService {

    private final TaskRepository taskRepository;

    public TaskReadinessService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<String> checkReadiness(Long taskId) {

        Task task = taskRepository.findById(taskId).orElse(null);

        List<String> issues = new ArrayList<>();

        if (task == null) {
            issues.add("Task not found");
            return issues;
        }

        // Basic task information
        if (task.getTitle() == null
                || task.getTitle().trim().isEmpty()) {

            issues.add("Task title is missing");
        }

        if (task.getObjective() == null
                || task.getObjective().trim().isEmpty()) {

            issues.add("Task objective is missing");
        }

        if (task.getRepository() == null
                || task.getRepository().trim().isEmpty()) {

            issues.add("Repository is missing");
        }

        if (task.getServiceName() == null
                || task.getServiceName().trim().isEmpty()) {

            issues.add("Service name is missing");
        }

        // AI execution contract
        if (task.getInputs() == null
                || task.getInputs().isEmpty()) {

            issues.add("Task inputs are missing");
        }

        if (task.getExecutionSteps() == null
                || task.getExecutionSteps().isEmpty()) {

            issues.add("Execution steps are missing");
        }

        if (task.getAcceptanceCriteria() == null
                || task.getAcceptanceCriteria().isEmpty()) {

            issues.add("Acceptance criteria are missing");
        }

        if (task.getRequiredTests() == null
                || task.getRequiredTests().isEmpty()) {

            issues.add("Required tests are missing");
        }

        if (task.getTimeoutMinutes() == null
                || task.getTimeoutMinutes() <= 0) {

            issues.add("Valid timeout is missing");
        }

        if (task.getMaxRetries() == null
                || task.getMaxRetries() < 0) {

            issues.add("Valid retry policy is missing");
        }

        if (task.getEscalationConditions() == null
                || task.getEscalationConditions().isEmpty()) {

            issues.add("Escalation conditions are missing");
        }

        // Dependency validation
        if (task.getDependencies() != null) {

            for (String dependencyId : task.getDependencies()) {

                try {

                    Long id = Long.parseLong(dependencyId);

                    Task dependency =
                            taskRepository.findById(id).orElse(null);

                    if (dependency == null) {

                        issues.add(
                                "Dependency task " + dependencyId
                                        + " does not exist"
                        );

                    } else if (!"COMPLETED".equalsIgnoreCase(
                            dependency.getStatus())) {

                        issues.add(
                                "Dependency task " + dependencyId
                                        + " is not completed"
                        );
                    }

                } catch (NumberFormatException e) {

                    issues.add(
                            "Invalid dependency ID: "
                                    + dependencyId
                    );
                }
            }
        }

        return issues;
    }

    public boolean isReady(Long taskId) {
        return checkReadiness(taskId).isEmpty();
    }
}