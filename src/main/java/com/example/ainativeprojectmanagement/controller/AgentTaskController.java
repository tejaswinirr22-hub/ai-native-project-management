package com.example.ainativeprojectmanagement.controller;

import com.example.ainativeprojectmanagement.service.AgentTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/agents")
public class AgentTaskController {

    private final AgentTaskService agentTaskService;

    public AgentTaskController(AgentTaskService agentTaskService) {
        this.agentTaskService = agentTaskService;
    }

    @PostMapping("/{agentId}/claim-task/{taskId}")
    public ResponseEntity<Map<String, Object>> claimTask(
            @PathVariable Long agentId,
            @PathVariable Long taskId) {

        String message = agentTaskService.claimTask(
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

    @PostMapping("/{agentId}/release-task/{taskId}")
    public ResponseEntity<Map<String, Object>> releaseTask(
            @PathVariable Long agentId,
            @PathVariable Long taskId) {

        String message = agentTaskService.releaseTask(
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