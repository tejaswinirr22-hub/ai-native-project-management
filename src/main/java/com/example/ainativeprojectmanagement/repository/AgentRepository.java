package com.example.ainativeprojectmanagement.repository;

import com.example.ainativeprojectmanagement.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long> {
}