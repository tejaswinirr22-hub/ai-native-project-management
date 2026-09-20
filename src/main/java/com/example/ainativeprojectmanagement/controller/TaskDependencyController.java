package com.example.ainativeprojectmanagement.controller;

import com.example.ainativeprojectmanagement.service.TaskDependencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskDependencyController {

    private final TaskDependencyService dependencyService;

    public TaskDependencyController(TaskDependencyService dependencyService) {
        this.dependencyService = dependencyService;
    }

    @GetMapping("/{id}/can-execute")
    public ResponseEntity<Map<String, Object>> canExecuteTask(
            @PathVariable Long id) {

        boolean canExecute = dependencyService.canExecuteTask(id);

        return ResponseEntity.ok(
                Map.of(
                        "taskId", id,
                        "canExecute", canExecute
                )
        );
    }
}