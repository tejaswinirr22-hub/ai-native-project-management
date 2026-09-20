package com.example.ainativeprojectmanagement.service;

import com.example.ainativeprojectmanagement.model.Task;
import com.example.ainativeprojectmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskDependencyService {

    private final TaskRepository taskRepository;

    public TaskDependencyService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public boolean canExecuteTask(Long taskId) {

        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            return false;
        }

        List<String> dependencies = task.getDependencies();

        if (dependencies == null || dependencies.isEmpty()) {
            return true;
        }

        for (String dependencyId : dependencies) {

            try {
                Long id = Long.parseLong(dependencyId);

                Task dependency =
                        taskRepository.findById(id).orElse(null);

                if (dependency == null) {
                    return false;
                }

                if (!"COMPLETED".equalsIgnoreCase(
                        dependency.getStatus())) {

                    return false;
                }

            } catch (NumberFormatException e) {

                return false;
            }
        }

        return true;
    }
}