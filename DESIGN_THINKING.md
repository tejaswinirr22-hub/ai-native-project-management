# Design Thinking Document

## AI-Native Project Management System

**Project:** AI-Native Project Management System  
**Technology:** Java, Spring Boot, Spring Data JPA, H2 Database  
**Project Type:** Full Stack Developer Intern Assignment – Problem Statement 2

---

# 1. Understanding of the Problem

The problem statement describes a software development environment where AI agents are responsible for major parts of the software development lifecycle.

Traditional project management systems such as Jira are primarily designed around human developers. A human can understand a task such as:

> "Fix the login bug."

An autonomous AI agent requires much more structured information before it can safely execute the work.

The agent needs to know:

- What needs to be changed
- Which repository contains the code
- Which service is affected
- What inputs are available
- What steps should be followed
- What tests must be executed
- What conditions define successful completion
- Which other tasks must be completed first
- How many times the task can be retried
- When human intervention is required

Therefore, the central design decision was to replace the traditional free-text ticket with an **Executable Task Contract**.

---

# 2. Design Goal

The main design goal was to create a project management backend where an AI agent can:

1. Discover a task.
2. Determine whether the task is ready.
3. Check its dependencies.
4. Claim the task.
5. Execute the task.
6. Record execution attempts.
7. Retry failures according to a defined policy.
8. Escalate to a human only when necessary.
9. Complete the task.
10. Produce a meaningful audit trail.

The system should also preserve enough context so that another agent can understand what happened previously.

---

# 3. Core Design Idea: Executable Task Contract

The most important design decision was to introduce a structured task contract.

A task is not represented only by a title and description.

Instead, it contains machine-readable information such as:

- Title
- Objective
- Repository
- Service name
- Inputs
- Execution steps
- Acceptance criteria
- Required tests
- Dependencies
- Retry policy
- Timeout
- Escalation conditions
- Current status

Example:

```text
Title:
Fix notification retry handling

Objective:
Update the notification service to handle failed email
notifications safely and preserve successful notification behavior.

Repository:
notification-backend

Service:
notification-service

Inputs:
- Notification service source code
- Existing notification tests
- Notification failure logs

Execution steps:
1. Inspect notification retry logic.
2. Identify the cause of failed notifications.
3. Implement failure-handling changes.
4. Run notification tests.
5. Verify successful notification behavior.

Acceptance criteria:
- Failed notifications are handled correctly.
- Successful notifications continue to work.
- Notification tests pass.

Required tests:
- Notification failure handling test
- Successful notification test
- Notification retry test
4. Why a Structured Contract Instead of a Normal Ticket?

A traditional ticket is optimized for human interpretation.

For example:

Fix login issue.

A human developer may ask questions and inspect the system manually.

An autonomous agent needs deterministic context.

Therefore, the system separates:

Human-readable information
        +
Machine-executable information

The task contract provides both.

This reduces ambiguity and allows the system to perform readiness validation before assigning work to an agent.

5. Readiness Before Execution

A major design decision was to prevent agents from starting incomplete tasks.

Before execution, the system checks whether the task contains the information required for execution.

The readiness check validates:

Task title
Objective
Repository
Service name
Inputs
Execution steps
Acceptance criteria
Required tests
Timeout
Retry policy
Escalation conditions
Dependencies

If any required information is missing, the task is considered not ready.

Example:

Task:
Fix login bug

Missing:
- Repository
- Service
- Acceptance criteria
- Required tests

The system should prevent an autonomous agent from executing such a task.

This follows the principle:

Do not allow an agent to execute an ambiguous task when the ambiguity can be detected before execution.

6. Dependency Handling

AI development work frequently contains dependencies.

For example:

Task 10
Fix notification retry handling
        |
        v
Task 11
Create notification analytics report

Task 11 depends on Task 10.

Therefore, Task 11 should not execute until Task 10 is completed.

The prototype represents dependencies using task IDs.

Example:

Task 11 dependencies:
["10"]

Before execution, the dependency service checks:

Does dependency task 10 exist?
Is task 10 completed?

If task 10 is not completed, task 11 remains blocked.

After task 10 becomes:

COMPLETED

task 11 becomes executable.

This provides a basic dependency graph.

7. Parallel Agent Execution

The system is designed around the idea that independent tasks can execute in parallel.

Example:

             Task A
            /      \
           /        \
       Task B      Task C
           \        /
            \      /
             Task D

Task B and Task C can potentially run simultaneously because they do not depend on each other.

Task D waits until both required upstream tasks are complete.

This model allows multiple AI agents to work concurrently while still respecting dependencies.

The current prototype demonstrates dependency gating using task IDs.

A production implementation would use a more advanced dependency graph and event-driven architecture.

8. Agent Assignment

Agents are represented separately from tasks.

An agent contains information such as:

Agent ID
Agent name
Agent type
Agent status
Current task

Example:

Agent:
CodeFixAgent

Type:
CODING_AGENT

Status:
IDLE

When the agent claims a task:

Agent status:
BUSY

Task status:
CLAIMED

This prevents the same agent from being assigned multiple tasks simultaneously.

9. Task Execution Lifecycle

The task lifecycle was designed as a state transition process.

Basic lifecycle:

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

Failure path:

RUNNING
   |
   v
FAILED
   |
   v
RETRY
   |
   v
RUNNING

If retries are exhausted:

FAILED
   |
   v
HUMAN_REVIEW

This provides a predictable execution model for autonomous agents.

10. Retry Strategy

Autonomous agents can fail for temporary or recoverable reasons.

Examples include:

Temporary build failure
Test failure
Dependency service unavailable
Temporary repository problem
Agent execution error

Instead of immediately involving a human, the system uses a retry policy.

Example:

Maximum retries = 3

Attempt 1 -> FAILED
Attempt 2 -> FAILED
Attempt 3 -> FAILED
                 |
                 v
           HUMAN_REVIEW

The retry count is stored with the task.

This allows the system to distinguish between:

Temporary failure

and:

Repeated failure requiring human attention
11. Human Escalation

Human intervention should not be the default path.

The system escalates only when predefined escalation conditions are reached.

Examples:

Repository is unavailable
Acceptance criteria cannot be verified
Tests continue failing after maximum retries
Required information is missing
Dependency remains unresolved

The human reviewer can then decide whether the task should be retried or handled manually.

This creates a human-in-the-loop model rather than a completely uncontrolled autonomous system.

12. Context Preservation

A major requirement of an AI-native system is preserving execution context.

When an agent fails, the next attempt should not start with zero knowledge.

The system therefore records execution attempts.

Each attempt can contain:

Task ID
Agent ID
Attempt number
Start time
End time
Status
Result
Error message
Tests executed
Files changed

Example:

Attempt 1
Status: FAILED
Error: Notification test failed

Attempt 2
Status: FAILED
Error: Retry validation failed

Attempt 3
Status: HUMAN_REVIEW

This information provides historical context for future agents and human reviewers.

13. Audit Trail

The system maintains an audit trail for important task events.

Examples:

TASK_CREATED
TASK_CLAIMED
TASK_STARTED
TASK_COMPLETED
TASK_FAILED
TASK_RETRY
HUMAN_REVIEW

Each audit event records:

Task ID
Agent ID when applicable
Action
Status
Message
Timestamp

This makes the system explainable.

Instead of only seeing:

Task completed

a user can understand:

10:08 - Task created
10:13 - Agent claimed task
10:14 - Agent started execution
10:15 - Agent completed task

The audit trail is therefore useful for both humans and AI agents.

14. Why Separate Execution Attempts and Audit Logs?

These two concepts serve different purposes.

Execution Attempt

Focuses on the technical execution of a task.

It answers:

What happened during this execution attempt?
Audit Log

Focuses on the lifecycle of the task.

It answers:

What happened to this task over time?

Keeping them separate makes the system easier to understand and extend.

15. Technology Tradeoffs
Spring Boot

Spring Boot was selected because:

It provides a structured backend architecture.
It supports REST APIs.
It integrates well with JPA.
It is suitable for enterprise applications.
It provides a good foundation for future production deployment.
Spring Data JPA

JPA was selected to simplify database persistence.

It provides:

Entity mapping
Repository abstraction
CRUD operations
Database integration
H2 Database

H2 was selected for the prototype because it is simple to configure and allows the system to run locally without requiring an external database server.

This reduces setup complexity during development.

For production, a database such as PostgreSQL would be preferred.

16. Synchronous REST API Tradeoff

The prototype uses REST APIs for operations such as:

Creating tasks
Checking readiness
Managing dependencies
Claiming tasks
Starting execution
Completing tasks
Recording failures
Managing human review
Viewing execution attempts
Viewing audit logs

This approach is simple and easy to demonstrate.

However, a production-scale AI-native system would benefit from asynchronous messaging.

For example:

Task Service
     |
     v
Message Queue
     |
     +--------> Agent 1
     |
     +--------> Agent 2
     |
     +--------> Agent 3

Technologies such as Kafka, RabbitMQ, or cloud messaging services could be introduced later.

17. Prototype vs Production

The current implementation focuses on demonstrating the core product concepts rather than implementing a complete enterprise platform.

Implemented in Prototype
Structured task contracts
Task readiness validation
Dependency checks
Agent management
Task claiming
Task execution lifecycle
Execution attempt tracking
Retry handling
Human escalation
Audit trail
REST APIs
Persistent H2 database
Production Enhancements
PostgreSQL
Redis
Kafka or another message broker
Authentication and authorization
Distributed locking
Real AI agent integration
Containerized deployment
Kubernetes
Observability
Metrics
Distributed tracing
Horizontal scaling
Real-time frontend
18. Security Considerations

An AI-native project management system controls software-development actions, so security is important.

A production version should implement:

Authentication
Role-based access control
Agent identity verification
API authorization
Secure repository access
Secrets management
Audit protection
Input validation
Rate limiting
Network security

Agents should also receive only the permissions required for their assigned work.

19. Concurrency Considerations

Multiple agents may attempt to claim tasks at the same time.

A production system therefore needs atomic task claiming.

For example:

Agent A ----\
             ---> Claim Task 10
Agent B ----/

Only one agent should successfully claim the task.

A production implementation could use:

Database transactions
Optimistic locking
Distributed locks
Redis
Queue-based assignment

The current prototype demonstrates the logical task-claiming flow, while distributed concurrency control is identified as a production enhancement.

20. Failure Isolation

One agent failure should not stop the entire project.

For example:

Agent 1 -> Task A -> FAILED

Agent 2 -> Task B -> COMPLETED

Agent 3 -> Task C -> RUNNING

Task A's failure should not automatically stop independent tasks B and C.

Only tasks that depend on Task A should be blocked.

This is important for autonomous parallel development.

21. Frontend Design

The current implementation focuses on the backend because the core challenge in the problem statement is the agent execution model.

A future frontend would provide:

Dashboard

Display:

Active tasks
Ready tasks
Blocked tasks
Running tasks
Failed tasks
Human review tasks
Task View

Display:

Task contract
Dependencies
Current agent
Execution history
Retry count
Acceptance criteria
Audit history
Dependency View

A graph visualization could show:

Task A
 |
 +----> Task B
 |
 +----> Task C
          |
          v
        Task D
Human Review View

Reviewers could see:

Why the task failed
Attempts made
Errors
Tests executed
Agent context
Available recovery actions
22. Deployment Strategy

The current project is designed to run locally using Spring Boot.

A production deployment could use:

                    Load Balancer
                         |
                         v
                Backend API Servers
                  /      |       \
                 /       |        \
                v        v         v
             Task     Agent      Audit
            Service   Service    Service
                \        |         /
                 \       |        /
                  v      v       v
                  Message Broker
                         |
              -----------------------
              |          |          |
              v          v          v
           Agent 1    Agent 2    Agent 3
                         |
                         v
                    PostgreSQL
                         |
                         v
                       Redis

The services could be containerized using Docker and deployed using Kubernetes or another cloud orchestration platform.

23. Important Design Tradeoffs
Simplicity vs Scalability

The prototype prioritizes simplicity so that the complete workflow can be demonstrated clearly.

Production would introduce distributed services and asynchronous communication.

H2 vs PostgreSQL

H2 reduces setup complexity.

PostgreSQL provides stronger production capabilities and scalability.

REST vs Event-Driven Architecture

REST is easier to implement and demonstrate.

Event-driven architecture is more suitable for large-scale autonomous agent coordination.

Automatic Retry vs Human Review

Automatic retries handle recoverable failures.

Human review handles persistent or ambiguous failures.

Rule-Based Readiness vs AI-Based Readiness

The current prototype uses deterministic rules.

A future system could use an AI evaluator to detect semantic ambiguity while keeping deterministic validation as a safety layer.

24. What I Would Do Differently With More Time

With additional development time, I would extend the prototype in the following areas:

1. Real AI Agent Integration

Connect the platform to real coding agents that can:

Read repositories
Modify code
Run tests
Create branches
Raise pull requests
Respond to test failures
2. Event-Driven Execution

Introduce Kafka or another message broker to distribute tasks to available agents.

3. Production Database

Replace H2 with PostgreSQL.

4. Distributed Locking

Use Redis or database-based locking to prevent duplicate task claims.

5. Rich Dependency Graph

Implement a proper graph representation instead of storing dependency IDs as simple strings.

6. Web Frontend

Build a React-based dashboard for task monitoring and human review.

7. Authentication

Add secure user and agent authentication.

8. Observability

Add:

Metrics
Logs
Distributed tracing
Health checks
Agent execution monitoring
9. Intelligent Readiness

Use an AI evaluator to identify ambiguous tasks before execution.

25. Known Limitations

The current implementation has several limitations.

The prototype uses H2 rather than a production database.
Agent execution is simulated through backend APIs rather than connected to real coding agents.
Dependencies are represented using task IDs rather than a dedicated graph database.
The current API is primarily synchronous.
Production-grade distributed locking is not implemented.
Authentication and authorization are not yet implemented.
The frontend is proposed as part of the architecture but is not included in the current backend prototype.
AI-based semantic readiness analysis is not implemented.
Large-scale distributed deployment has not been implemented.
Real repository and pull-request integrations are future enhancements.

These limitations are intentional because the prototype focuses on demonstrating the core AI-native project management concepts.

26. Proposed New Primitive: Executable Work Contract

The key product idea introduced by this design is the:

Executable Work Contract

Instead of treating a ticket as only a record of work, the system treats it as an executable contract between the project system and an AI agent.

The contract defines:

WHAT
    Objective

WHERE
    Repository / Service

WITH WHAT
    Inputs

HOW
    Execution Steps

DONE WHEN
    Acceptance Criteria

VERIFY WITH
    Required Tests

WAIT FOR
    Dependencies

FAILURE POLICY
    Retry Policy

ESCALATE WHEN
    Escalation Conditions

This can become a foundational primitive for AI-native software development.

27. Overall Design Philosophy

The overall philosophy is:

Traditional Project Management
            |
            v
Human-readable tickets
            |
            v
Human interpretation
            |
            v
Manual execution

The proposed AI-native approach is:

Executable Work Contract
            |
            v
Readiness Validation
            |
            v
Dependency Resolution
            |
            v
Agent Assignment
            |
            v
Autonomous Execution
            |
            v
Tests + Acceptance Criteria
            |
       +----+----+
       |         |
     Success   Failure
       |         |
       v         v
   Complete    Retry
                 |
                 v
          Human Review

This design allows the project management system to become an orchestration layer for autonomous software-development agents.

28. Final Design Outcome

The system demonstrates how traditional project management concepts can be adapted for an AI-driven development environment.

The major design decisions are:

Replace ambiguous tickets with executable task contracts.
Validate readiness before assigning work.
Represent dependencies explicitly.
Allow independent tasks to execute in parallel.
Track agent ownership.
Track every execution attempt.
Retry recoverable failures automatically.
Escalate persistent or ambiguous failures to humans.
Maintain an audit trail.
Preserve execution context for future agents.
Keep the prototype simple while defining a path toward production scale.
Introduce the Executable Work Contract as a new AI-native project-management primitive.