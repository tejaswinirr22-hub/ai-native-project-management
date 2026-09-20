# AI-Native Project Management System

## Overview

The AI-Native Project Management System is a backend prototype designed for software development environments where autonomous AI agents perform development tasks.

Traditional project management tools are mainly designed for human developers using tickets, boards, and sprints. AI coding agents require a more structured and machine-readable system that defines exactly what they need to do, what inputs they can use, what tests must pass, how failures should be handled, and when human intervention is required.

This project provides a backend foundation for such an AI-native project management system.

---

## Problem Statement

The goal is to redesign traditional project management concepts for an agentic AI development workflow.

The system should allow AI agents to:

* Understand machine-readable tasks
* Check whether a task is ready for execution
* Respect task dependencies
* Claim tasks
* Start task execution
* Track execution attempts
* Retry failed tasks
* Escalate repeated failures to humans
* Preserve execution context
* Record an audit trail
* Complete tasks only after successful execution

---

## Key Features

### 1. Executable Task Contracts

Instead of a simple Jira-style ticket, each task contains structured information required by an AI agent.

A task can contain:

* Title
* Objective
* Repository
* Service name
* Inputs
* Execution steps
* Acceptance criteria
* Required tests
* Dependencies
* Retry policy
* Timeout
* Escalation conditions
* Execution status

Example:

```json
{
  "title": "Fix notification retry handling",
  "objective": "Update the notification service to handle failed email notifications safely.",
  "repository": "notification-backend",
  "serviceName": "notification-service",
  "inputs": [
    "Notification service source code",
    "Existing notification tests",
    "Notification failure logs"
  ],
  "executionSteps": [
    "Inspect the notification retry logic",
    "Identify the cause of failed email notifications",
    "Implement the required failure-handling changes",
    "Run the notification tests",
    "Verify successful notification behavior"
  ],
  "acceptanceCriteria": [
    "Failed email notifications are handled correctly",
    "Successful notifications continue to work",
    "Notification tests pass"
  ],
  "requiredTests": [
    "Notification failure handling test",
    "Successful notification test",
    "Notification retry test"
  ],
  "maxRetries": 3,
  "timeoutMinutes": 30
}
```

---

## 2. Task Readiness and Ambiguity Detection

Before an agent executes a task, the system checks whether the task contains enough information.

The readiness check verifies:

* Task title
* Objective
* Repository
* Service name
* Inputs
* Execution steps
* Acceptance criteria
* Required tests
* Timeout
* Retry policy
* Escalation conditions
* Dependency status

If required information is missing, the task is marked as not ready.

Example response:

```json
{
  "taskId": 10,
  "ready": true,
  "requiresHumanReview": false,
  "issues": []
}
```

---

## 3. Task Dependencies

Tasks can depend on other tasks.

An agent cannot execute a dependent task until all required dependencies are completed.

For example:

```text
Task 10
   |
   v
Task 11
```

Task 11 depends on Task 10.

Before Task 10 was completed:

```json
{
  "taskId": 11,
  "canExecute": false
}
```

After Task 10 was completed:

```json
{
  "taskId": 11,
  "canExecute": true
}
```

This prevents agents from executing tasks using incomplete or unavailable work.

---

## 4. AI Agent Management

The system supports AI agents that can claim and execute tasks.

An agent has:

* ID
* Name
* Type
* Status
* Current task

Example:

```json
{
  "id": 1,
  "name": "CodeFixAgent",
  "type": "CODING_AGENT",
  "status": "IDLE",
  "currentTaskId": null
}
```

Agents can:

1. Claim a task
2. Start execution
3. Complete the task
4. Report failure
5. Retry when allowed
6. Escalate to human review

---

## 5. Task Execution

Once an agent claims a task and the task is ready, execution can begin.

Typical lifecycle:

```text
PENDING
   |
   v
CLAIMED
   |
   v
RUNNING
   |
   v
COMPLETED
```

The system records execution information such as:

* Agent
* Start time
* End time
* Attempt number
* Result
* Status
* Error message
* Files changed
* Tests executed

---

## 6. Failure and Retry Handling

AI agents can fail during execution.

The system supports configurable retry policies.

Example:

```text
Attempt 1
   |
   v
FAILED
   |
   v
RETRY

Attempt 2
   |
   v
FAILED
   |
   v
RETRY

Attempt 3
   |
   v
FAILED
   |
   v
HUMAN_REVIEW
```

The maximum retry count is defined as part of the task contract.

This prevents an agent from repeatedly failing without a controlled escalation mechanism.

---

## 7. Human Escalation

If an agent repeatedly fails a task, the system can escalate the task to a human reviewer.

Human review can be used when:

* Maximum retries are reached
* Required repository information is unavailable
* Acceptance criteria cannot be verified
* Tests continue failing
* The agent cannot safely continue

A human reviewer can approve the task for another retry.

Example:

```text
Agent Failure
      |
      v
Retry Limit Reached
      |
      v
Human Review
      |
      +----> Approve Retry
      |
      +----> Keep Under Review
```

---

## 8. Execution Attempts

Every execution attempt is stored separately.

This provides a history of what happened during each attempt.

Example:

```json
{
  "attemptNumber": 1,
  "status": "COMPLETED",
  "result": "Task successfully completed",
  "errorMessage": null
}
```

This makes it possible to understand how an AI agent reached the final task state.

---

## 9. Audit Trail

The system maintains an audit trail for important task events.

Examples include:

* TASK_CREATED
* TASK_CLAIMED
* TASK_STARTED
* TASK_FAILED
* TASK_COMPLETED
* HUMAN_REVIEW
* RETRY_APPROVED

Example:

```json
{
  "action": "TASK_COMPLETED",
  "agentId": 1,
  "taskId": 10,
  "status": "COMPLETED",
  "message": "Task successfully completed by agent"
}
```

The audit trail provides traceability and accountability for autonomous agent activity.

---

# System Architecture

The project follows a layered Spring Boot architecture.

```text
Client / Postman
       |
       v
REST Controllers
       |
       v
Service Layer
       |
       v
Repository Layer
       |
       v
H2 Database
```

### Main Layers

**Controller Layer**

Handles REST API requests.

**Service Layer**

Contains business logic such as:

* Task creation
* Task readiness
* Dependency validation
* Agent assignment
* Execution
* Completion
* Failure handling
* Retry handling
* Human escalation
* Audit logging

**Repository Layer**

Uses Spring Data JPA to communicate with the database.

**Model Layer**

Contains JPA entities representing:

* Task
* Agent
* AuditLog
* ExecutionAttempt

---

# Technology Stack

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Maven
* H2 Database
* Jakarta Persistence
* REST APIs
* Postman
* Git
* GitHub

---

# Project Structure

```text
ai-native-project-management
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.ainativeprojectmanagement
│   │   │       │
│   │   │       ├── controller
│   │   │       │   ├── TaskController.java
│   │   │       │   ├── TaskDependencyController.java
│   │   │       │   ├── TaskReadinessController.java
│   │   │       │   ├── AgentController.java
│   │   │       │   ├── AgentTaskController.java
│   │   │       │   ├── TaskExecutionController.java
│   │   │       │   ├── TaskCompletionController.java
│   │   │       │   ├── TaskFailureController.java
│   │   │       │   ├── ExecutionAttemptController.java
│   │   │       │   ├── HumanReviewController.java
│   │   │       │   └── AuditLogController.java
│   │   │       │
│   │   │       ├── model
│   │   │       │   ├── Task.java
│   │   │       │   ├── Agent.java
│   │   │       │   ├── AuditLog.java
│   │   │       │   └── ExecutionAttempt.java
│   │   │       │
│   │   │       ├── repository
│   │   │       │   ├── TaskRepository.java
│   │   │       │   ├── AgentRepository.java
│   │   │       │   ├── AuditLogRepository.java
│   │   │       │   └── ExecutionAttemptRepository.java
│   │   │       │
│   │   │       └── service
│   │   │           ├── TaskService.java
│   │   │           ├── TaskDependencyService.java
│   │   │           ├── TaskReadinessService.java
│   │   │           ├── AgentService.java
│   │   │           ├── AgentTaskService.java
│   │   │           ├── TaskExecutionService.java
│   │   │           ├── TaskCompletionService.java
│   │   │           ├── TaskFailureService.java
│   │   │           ├── ExecutionAttemptService.java
│   │   │           ├── HumanReviewService.java
│   │   │           └── AuditLogService.java
│   │   │
│   │   └── resources
│   │       └── application.properties
│   │
│   └── test
│
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
└── README.md
```

---

# REST API Endpoints

## Task APIs

```text
POST   /api/tasks
GET    /api/tasks
GET    /api/tasks/{id}
PUT    /api/tasks/{id}
DELETE /api/tasks/{id}
```

## Task Dependency

```text
GET /api/tasks/{id}/can-execute
```

Checks whether all task dependencies are completed.

## Task Readiness

```text
GET /api/tasks/{id}/readiness
```

Checks whether the task contains sufficient information for execution.

## Agent APIs

```text
POST /api/agents
GET  /api/agents
```

## Agent Task Assignment

```text
POST /api/agents/{agentId}/tasks/{taskId}/claim
```

Assigns a task to an available agent.

## Task Execution

```text
POST /api/tasks/{taskId}/execute
```

Starts task execution.

## Task Completion

```text
POST /api/tasks/{taskId}/complete
```

Marks the task as successfully completed.

## Task Failure

```text
POST /api/tasks/{taskId}/fail
```

Records a task failure and applies the retry policy.

## Execution Attempts

```text
GET /api/execution-attempts/task/{taskId}
```

Returns execution history for a task.

## Human Review

```text
GET  /api/human-review/tasks
POST /api/human-review/tasks/{taskId}/approve-retry
```

Used to review and approve retry after escalation.

## Audit Logs

```text
GET /api/audit-logs/task/{taskId}
```

Returns the audit history for a task.

---

# Database

The prototype uses an H2 file-based database.

Database configuration:

```text
jdbc:h2:file:./data/ai_native_pms
```

The database is stored locally during development.

The `data/` directory is excluded from Git using `.gitignore`.

---

# How to Run

## Prerequisites

Install:

* Java 21
* Maven
* Git
* IntelliJ IDEA or another Java IDE
* Postman

## Start the Application

From the project directory:

```text
mvnw spring-boot:run
```

On Windows:

```text
mvnw.cmd spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

---

# Demonstrated Workflow

The prototype demonstrates an agentic development workflow.

### Step 1 — Create an executable task

A structured task contract is created with objective, inputs, execution steps, acceptance criteria, tests, retry policy, and escalation conditions.

### Step 2 — Check readiness

The system validates whether the task has sufficient information.

### Step 3 — Create an AI agent

An agent is registered with the system.

### Step 4 — Claim the task

The available agent claims the task.

### Step 5 — Start execution

The task changes to the running state and an execution attempt is created.

### Step 6 — Complete the task

After successful execution, the task becomes completed.

### Step 7 — Dependency becomes available

A task depending on the completed task can now execute.

---

# Example Dependency Workflow

```text
Task 10
"Fix notification retry handling"
       |
       | COMPLETED
       v
Task 11
"Create notification analytics report"
```

Before Task 10 completion:

```text
Task 11
canExecute = false
```

After Task 10 completion:

```text
Task 11
canExecute = true
```

This demonstrates dependency-aware task execution.

---

# Example Failure Workflow

```text
Task
 |
 v
Agent starts execution
 |
 v
Failure
 |
 v
Retry
 |
 v
Failure
 |
 v
Retry
 |
 v
Failure
 |
 v
Human Review
 |
 v
Human approves retry
 |
 v
Task becomes available again
```

This demonstrates controlled autonomous execution with human-in-the-loop escalation.

---

# Why an Executable Task Contract?

Traditional project-management tickets are primarily written for humans.

For autonomous agents, a task should behave more like an executable contract.

Instead of only describing:

```text
Fix notification issue
```

the system provides:

```text
Objective
Inputs
Repository
Execution steps
Acceptance criteria
Required tests
Dependencies
Timeout
Retry policy
Escalation conditions
```

This allows an AI agent to understand the expected work and the conditions under which it can safely execute.

---

# AI-Native SDLC Primitive

The project introduces the concept of an **Executable Task Contract** as a primitive for agentic software development.

The contract connects:

```text
Intent
  |
  v
Task Contract
  |
  v
Agent Execution
  |
  v
Tests / Validation
  |
  v
Completion or Retry
  |
  v
Human Escalation
```

This is different from simply tracking tickets on a board because the task contains the information needed for autonomous execution and verification.

---

# Human-in-the-Loop

Autonomous execution should not mean that humans are completely removed from the process.

The system provides controlled human intervention when an agent cannot safely complete a task.

The human can review the situation and allow another retry.

This creates a balance between:

* Autonomous execution
* Automated validation
* Controlled retries
* Human oversight
* Auditability

---

# Current Prototype Scope

The current implementation focuses on the backend workflow:

* Executable task contracts
* Task readiness
* Dependency management
* Agent management
* Task claiming
* Task execution
* Execution attempts
* Failure handling
* Retry policies
* Human escalation
* Audit logging

The prototype uses REST APIs and Postman for demonstration.

A separate frontend is not required for the current backend prototype.

---

# Future Enhancements

Possible future improvements include:

* Real AI/LLM agent integration
* GitHub/GitLab integration
* Automatic repository inspection
* Automatic code execution
* Automated test execution
* Real-time agent monitoring
* Event-driven architecture
* Message queues
* Distributed execution
* Role-based access control
* Authentication and authorization
* Persistent execution context
* Vector database for agent memory
* Advanced dependency graphs
* Agent capability matching
* Parallel task execution
* Human review dashboard
* Web-based project management interface
* Docker and cloud deployment

---

# Conclusion

The AI-Native Project Management System demonstrates how traditional project management can be adapted for autonomous AI software-development agents.

The system treats development work as structured, executable contracts rather than simple tickets.

By combining machine-readable tasks, dependency management, readiness checks, autonomous agent execution, retry policies, human escalation, execution tracking, and audit logs, the prototype provides a foundation for an AI-native software development lifecycle.

---

## Author

**Tejaswini R R**

AI-Native Project Management System
Java | Spring Boot | REST API | JPA | H2 | Maven
