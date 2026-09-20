package com.example.ainativeprojectmanagement.service;

import com.example.ainativeprojectmanagement.model.AuditLog;
import com.example.ainativeprojectmanagement.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog createLog(
            Long taskId,
            Long agentId,
            String action,
            String status,
            String message) {

        AuditLog log = new AuditLog();

        log.setTaskId(taskId);
        log.setAgentId(agentId);
        log.setAction(action);
        log.setStatus(status);
        log.setMessage(message);
        log.setTimestamp(LocalDateTime.now());

        return auditLogRepository.save(log);
    }

    public List<AuditLog> getTaskHistory(Long taskId) {
        return auditLogRepository.findByTaskIdOrderByTimestampAsc(taskId);
    }

    public List<AuditLog> getAgentHistory(Long agentId) {
        return auditLogRepository.findByAgentIdOrderByTimestampAsc(agentId);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}