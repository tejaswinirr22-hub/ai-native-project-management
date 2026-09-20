package com.example.ainativeprojectmanagement.controller;

import com.example.ainativeprojectmanagement.model.ExecutionAttempt;
import com.example.ainativeprojectmanagement.service.ExecutionAttemptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/execution-attempts")
public class ExecutionAttemptController {

    private final ExecutionAttemptService executionAttemptService;

    public ExecutionAttemptController(
            ExecutionAttemptService executionAttemptService) {

        this.executionAttemptService = executionAttemptService;
    }

    @PostMapping
    public ResponseEntity<ExecutionAttempt> createAttempt(
            @RequestParam Long taskId,
            @RequestParam Long agentId,
            @RequestParam Integer attemptNumber) {

        return ResponseEntity.ok(
                executionAttemptService.createAttempt(
                        taskId,
                        agentId,
                        attemptNumber
                )
        );
    }

    @PutMapping("/{attemptId}")
    public ResponseEntity<ExecutionAttempt> updateAttempt(
            @PathVariable Long attemptId,
            @RequestParam String status,
            @RequestParam(required = false) String errorMessage,
            @RequestParam(required = false) String filesChanged,
            @RequestParam(required = false) String testsExecuted,
            @RequestParam(required = false) String result) {

        ExecutionAttempt updatedAttempt =
                executionAttemptService.updateAttempt(
                        attemptId,
                        status,
                        errorMessage,
                        filesChanged,
                        testsExecuted,
                        result
                );

        if (updatedAttempt == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedAttempt);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<ExecutionAttempt>> getTaskAttempts(
            @PathVariable Long taskId) {

        return ResponseEntity.ok(
                executionAttemptService.getTaskAttempts(taskId)
        );
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<ExecutionAttempt>> getAgentAttempts(
            @PathVariable Long agentId) {

        return ResponseEntity.ok(
                executionAttemptService.getAgentAttempts(agentId)
        );
    }
}
