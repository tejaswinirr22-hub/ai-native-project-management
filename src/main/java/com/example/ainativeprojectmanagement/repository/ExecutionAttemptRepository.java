package com.example.ainativeprojectmanagement.repository;

import com.example.ainativeprojectmanagement.model.ExecutionAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecutionAttemptRepository
        extends JpaRepository<ExecutionAttempt, Long> {

    List<ExecutionAttempt> findByTaskIdOrderByAttemptNumberAsc(Long taskId);

    List<ExecutionAttempt> findByAgentIdOrderByStartTimeAsc(Long agentId);
}