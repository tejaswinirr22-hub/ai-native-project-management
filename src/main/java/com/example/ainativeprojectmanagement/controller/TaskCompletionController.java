package com.example.ainativeprojectmanagement.controller;

import com.example.ainativeprojectmanagement.service.TaskCompletionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/agents")
public class TaskCompletionController {

    private final TaskCompletionService taskCompletionService;

    public TaskCompletionController(
            TaskCompletionService taskCompletionService) {
        this.taskCompletionService = taskCompletionService;
    }

    @PostMapping("/{agentId}/complete-task/{taskId}")
    public ResponseEntity<Map<String, Object>> completeTask(
            @PathVariable Long agentId,
            @PathVariable Long taskId) {

        String message = taskCompletionService.completeTask(
                agentId,
                taskId
        );

        return ResponseEntity.ok(
                Map.of(
                        "agentId", agentId,
                        "taskId", taskId,
                        "message", message
                )
        );
    }
}