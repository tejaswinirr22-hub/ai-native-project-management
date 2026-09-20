package com.example.ainativeprojectmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "execution_attempts")
public class ExecutionAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long taskId;

    private Long agentId;

    private Integer attemptNumber;

    private String status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(length = 3000)
    private String errorMessage;

    @Column(length = 3000)
    private String filesChanged;

    @Column(length = 3000)
    private String testsExecuted;

    @Column(length = 3000)
    private String result;

    public ExecutionAttempt() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Integer getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getFilesChanged() {
        return filesChanged;
    }

    public void setFilesChanged(String filesChanged) {
        this.filesChanged = filesChanged;
    }

    public String getTestsExecuted() {
        return testsExecuted;
    }

    public void setTestsExecuted(String testsExecuted) {
        this.testsExecuted = testsExecuted;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}