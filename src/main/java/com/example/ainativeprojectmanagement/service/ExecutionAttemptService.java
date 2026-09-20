package com.example.ainativeprojectmanagement.service;

import com.example.ainativeprojectmanagement.model.ExecutionAttempt;
import com.example.ainativeprojectmanagement.repository.ExecutionAttemptRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExecutionAttemptService {

    private final ExecutionAttemptRepository executionAttemptRepository;

    public ExecutionAttemptService(
            ExecutionAttemptRepository executionAttemptRepository) {

        this.executionAttemptRepository = executionAttemptRepository;
    }

    // Create a new execution attempt
    public ExecutionAttempt createAttempt(
            Long taskId,
            Long agentId,
            Integer attemptNumber) {

        ExecutionAttempt attempt = new ExecutionAttempt();

        attempt.setTaskId(taskId);
        attempt.setAgentId(agentId);
        attempt.setAttemptNumber(attemptNumber);
        attempt.setStatus("RUNNING");
        attempt.setStartTime(LocalDateTime.now());

        return executionAttemptRepository.save(attempt);
    }

    // Update a specific execution attempt
    public ExecutionAttempt updateAttempt(
            Long attemptId,
            String status,
            String errorMessage,
            String filesChanged,
            String testsExecuted,
            String result) {

        ExecutionAttempt attempt =
                executionAttemptRepository
                        .findById(attemptId)
                        .orElse(null);

        if (attempt == null) {
            return null;
        }

        attempt.setStatus(status);
        attempt.setErrorMessage(errorMessage);
        attempt.setFilesChanged(filesChanged);
        attempt.setTestsExecuted(testsExecuted);
        attempt.setResult(result);
        attempt.setEndTime(LocalDateTime.now());

        return executionAttemptRepository.save(attempt);
    }

    // Get all execution attempts for a task
    public List<ExecutionAttempt> getTaskAttempts(Long taskId) {

        return executionAttemptRepository
                .findByTaskIdOrderByAttemptNumberAsc(taskId);
    }

    // Get all execution attempts for an agent
    public List<ExecutionAttempt> getAgentAttempts(Long agentId) {

        return executionAttemptRepository
                .findByAgentIdOrderByStartTimeAsc(agentId);
    }

    // Automatically update the latest execution attempt
    public ExecutionAttempt updateLatestAttempt(
            Long taskId,
            String status,
            String errorMessage,
            String filesChanged,
            String testsExecuted,
            String result) {

        List<ExecutionAttempt> attempts =
                executionAttemptRepository
                        .findByTaskIdOrderByAttemptNumberAsc(taskId);

        if (attempts.isEmpty()) {
            return null;
        }

        ExecutionAttempt latestAttempt =
                attempts.get(attempts.size() - 1);

        latestAttempt.setStatus(status);
        latestAttempt.setErrorMessage(errorMessage);
        latestAttempt.setFilesChanged(filesChanged);
        latestAttempt.setTestsExecuted(testsExecuted);
        latestAttempt.setResult(result);
        latestAttempt.setEndTime(LocalDateTime.now());

        return executionAttemptRepository.save(latestAttempt);
    }
}