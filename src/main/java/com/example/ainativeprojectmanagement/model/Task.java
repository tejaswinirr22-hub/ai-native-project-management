package com.example.ainativeprojectmanagement.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String objective;

    private String repository;

    private String serviceName;

    private String status;

    private Integer retryCount;

    private Integer maxRetries;

    private Integer timeoutMinutes;

    @ElementCollection
    private List<String> inputs;

    @ElementCollection
    private List<String> executionSteps;

    @ElementCollection
    private List<String> acceptanceCriteria;

    @ElementCollection
    private List<String> requiredTests;

    @ElementCollection
    private List<String> dependencies;

    @ElementCollection
    private List<String> escalationConditions;

    public Task() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Integer getTimeoutMinutes() {
        return timeoutMinutes;
    }

    public void setTimeoutMinutes(Integer timeoutMinutes) {
        this.timeoutMinutes = timeoutMinutes;
    }

    public List<String> getInputs() {
        return inputs;
    }

    public void setInputs(List<String> inputs) {
        this.inputs = inputs;
    }

    public List<String> getExecutionSteps() {
        return executionSteps;
    }

    public void setExecutionSteps(List<String> executionSteps) {
        this.executionSteps = executionSteps;
    }

    public List<String> getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public void setAcceptanceCriteria(List<String> acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }

    public List<String> getRequiredTests() {
        return requiredTests;
    }

    public void setRequiredTests(List<String> requiredTests) {
        this.requiredTests = requiredTests;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public List<String> getEscalationConditions() {
        return escalationConditions;
    }

    public void setEscalationConditions(List<String> escalationConditions) {
        this.escalationConditions = escalationConditions;
    }
}