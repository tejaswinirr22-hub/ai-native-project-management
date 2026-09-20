package com.example.ainativeprojectmanagement.controller;

import com.example.ainativeprojectmanagement.service.TaskReadinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskReadinessController {

    private final TaskReadinessService taskReadinessService;

    public TaskReadinessController(
            TaskReadinessService taskReadinessService) {

        this.taskReadinessService = taskReadinessService;
    }

    @GetMapping("/{taskId}/readiness")
    public ResponseEntity<Map<String, Object>> checkReadiness(
            @PathVariable Long taskId) {

        List<String> issues =
                taskReadinessService.checkReadiness(taskId);

        boolean ready = issues.isEmpty();

        return ResponseEntity.ok(
                Map.of(
                        "taskId", taskId,
                        "ready", ready,
                        "requiresHumanReview", !ready,
                        "issues", issues
                )
        );
    }
}