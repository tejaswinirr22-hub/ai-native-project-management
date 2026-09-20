package com.example.ainativeprojectmanagement.controller;

import com.example.ainativeprojectmanagement.service.TaskFailureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/agents")
public class TaskFailureController {

    private final TaskFailureService taskFailureService;

    public TaskFailureController(TaskFailureService taskFailureService) {
        this.taskFailureService = taskFailureService;
    }

    @PostMapping("/{agentId}/fail-task/{taskId}")
    public ResponseEntity<Map<String, Object>> failTask(
            @PathVariable Long agentId,
            @PathVariable Long taskId,
            @RequestParam String error) {

        String message = taskFailureService.failTask(
                agentId,
                taskId,
                error
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