package com.example.ainativeprojectmanagement.service;

import com.example.ainativeprojectmanagement.model.Agent;
import com.example.ainativeprojectmanagement.repository.AgentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService {

    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public Agent createAgent(Agent agent) {

        if (agent.getStatus() == null) {
            agent.setStatus("IDLE");
        }

        return agentRepository.save(agent);
    }

    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    public Optional<Agent> getAgentById(Long id) {
        return agentRepository.findById(id);
    }
}