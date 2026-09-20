package com.example.ainativeprojectmanagement.repository;

import com.example.ainativeprojectmanagement.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByTaskIdOrderByTimestampAsc(Long taskId);

    List<AuditLog> findByAgentIdOrderByTimestampAsc(Long agentId);
}