package com.example.ainativeprojectmanagement.controller;

import com.example.ainativeprojectmanagement.service.TaskExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskExecutionController {

    private final TaskExecutionService taskExecutionService;

    public TaskExecutionController(
            TaskExecutionService taskExecutionService) {

        this.taskExecutionService = taskExecutionService;
    }

    @PostMapping("/{taskId}/start")
    public ResponseEntity<Map<String, Object>> startTask(
            @PathVariable Long taskId,
            @RequestParam Long agentId) {

        String message = taskExecutionService.startTask(
                taskId,
                agentId
        );

        return ResponseEntity.ok(
                Map.of(
                        "taskId", taskId,
                        "agentId", agentId,
                        "message", message
                )
        );
    }
}