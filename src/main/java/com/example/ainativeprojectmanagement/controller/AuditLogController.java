package com.example.ainativeprojectmanagement.controller;

import com.example.ainativeprojectmanagement.model.AuditLog;
import com.example.ainativeprojectmanagement.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<AuditLog>> getTaskHistory(
            @PathVariable Long taskId) {

        return ResponseEntity.ok(
                auditLogService.getTaskHistory(taskId)
        );
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<AuditLog>> getAgentHistory(
            @PathVariable Long agentId) {

        return ResponseEntity.ok(
                auditLogService.getAgentHistory(agentId)
        );
    }
}