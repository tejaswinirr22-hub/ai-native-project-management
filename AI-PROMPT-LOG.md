# AI Prompt Log

## AI-Native Project Management System

**Project:** AI-Native Project Management System  
**AI Tool Used:** ChatGPT  
**Purpose:** Development assistance, problem understanding, architecture design, coding support, debugging, documentation, and Git/GitHub guidance.

---

# 1. Purpose of This Prompt Log

AI assistance was used during the development of this project as a development support tool.

The AI was used primarily to:

- Understand the problem statement
- Break the requirements into smaller implementation tasks
- Design the backend architecture
- Define the executable task contract
- Generate and improve Java/Spring Boot code
- Identify implementation issues
- Design REST APIs
- Plan testing workflows
- Prepare project documentation
- Provide Git and GitHub guidance

The final implementation decisions were reviewed and applied as part of the project development process.

---

# 2. Problem Statement Understanding

### Prompt Purpose

The first use of AI was to understand the AI-Native Project Management System problem statement and convert the requirements into implementable system features.

### Representative Prompt

```text
Explain the AI-Native Project Management System problem statement
in simple terms and identify what needs to be designed and built.
Break the requirements into backend features that can be implemented
in a Java Spring Boot project.
AI Assistance Used For

The problem statement was broken into the following major capabilities:

Machine-readable task structure
Task readiness
Dependency management
Agent assignment
Autonomous task execution
Failure handling
Retry management
Human escalation
Execution attempt tracking
Audit trail
3. Executable Task Contract
Prompt Purpose

The problem statement required tasks to contain enough information for an autonomous agent to execute them.

Representative Prompt
How should I redesign a Jira-style task so that an AI coding
agent can understand the task and execute it autonomously?
What fields should the task contain?
Resulting Design

The task contract was designed with fields including:

Title
Objective
Repository
Service name
Inputs
Execution steps
Acceptance criteria
Required tests
Dependencies
Retry count
Maximum retries
Timeout
Escalation conditions
Status

This became the central data structure of the application.

4. Task Entity Design
Prompt Purpose

AI assistance was used to translate the executable task contract into a Java entity.

Representative Prompt
Create a Spring Boot JPA Task entity for an AI-native project
management system. It should support task objectives,
repository, service, inputs, execution steps, acceptance criteria,
required tests, dependencies, retry policy, timeout and escalation.
Resulting Implementation

A Task JPA entity was created with persistent fields and collection fields for:

Inputs
Execution steps
Acceptance criteria
Required tests
Dependencies
Escalation conditions
5. Task Readiness
Prompt Purpose

The problem statement requires the system to identify whether a task is sufficiently defined before an AI agent executes it.

Representative Prompt
Design a readiness check for an AI-executable task.
What information should be validated before an agent is allowed
to execute the task?
Resulting Design

A TaskReadinessService was implemented.

It checks:

Title
Objective
Repository
Service
Inputs
Execution steps
Acceptance criteria
Required tests
Timeout
Retry policy
Escalation conditions
Dependencies

If required information is missing, the task is marked as not ready.

6. Dependency Management
Prompt Purpose

The problem statement specifically requires handling dependencies between tasks.

Representative Prompt
How should task dependencies work in an AI-native project
management system? If Task B depends on Task A, how should the
system prevent Task B from executing until Task A is completed?
Resulting Design

A dependency service was created.

The system checks:

Whether the dependency task exists.
Whether the dependency task is completed.
Whether all dependencies are satisfied.

Example:

Task 10
   |
   v
Task 11

Task 11 remains blocked while Task 10 is incomplete.

Once Task 10 becomes COMPLETED, Task 11 can execute.

7. Agent Management
Prompt Purpose

AI assistance was used to design how autonomous agents interact with tasks.

Representative Prompt
Design a simple agent management system where coding agents
can claim tasks, become busy while executing, and become idle
after completing the task.
Resulting Design

An Agent entity and related services were used.

The agent lifecycle includes:

IDLE
 |
 v
CLAIM TASK
 |
 v
BUSY
 |
 v
TASK COMPLETED
 |
 v
IDLE

This establishes ownership of a task during execution.

8. Task Claiming
Prompt Purpose

The problem statement requires agents to pick up tasks autonomously.

Representative Prompt
How should an AI agent claim an available task while respecting
task readiness and dependencies?
Resulting Design

The claim workflow verifies that:

The task exists.
The task is ready.
Dependencies are satisfied.
The agent is available.

The task is then associated with the agent and moved to the appropriate state.

9. Task Execution Lifecycle
Prompt Purpose

AI assistance was used to define the lifecycle of autonomous task execution.

Representative Prompt
Design a task execution lifecycle for an AI agent.
Include pending, claimed, running, completed and failed states.
Resulting Design

The lifecycle was implemented conceptually as:

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
10. Failure and Retry Handling
Prompt Purpose

The problem statement requires autonomous agents to recover from failures.

Representative Prompt
How should an AI-native task system handle agent failures?
Implement retry logic using maximum retries and escalate to
human review after retries are exhausted.
Resulting Design

The system tracks:

Current retry count
Maximum retry count
Failure status
Execution attempts
Human review state

Example:

Attempt 1 -> FAILED
Attempt 2 -> FAILED
Attempt 3 -> FAILED
                 |
                 v
          HUMAN_REVIEW
11. Human Escalation
Prompt Purpose

The problem statement requires human intervention only when necessary.

Representative Prompt
When should an autonomous coding task be escalated to a human?
How should the system represent escalation conditions?
Resulting Design

Tasks contain explicit escalation conditions.

Examples include:

Repository unavailable
Acceptance criteria cannot be verified
Tests continue failing after maximum retries
Required information is unavailable

This allows the system to distinguish normal autonomous execution from situations requiring human intervention.

12. Execution Attempt Tracking
Prompt Purpose

The problem statement requires context preservation across retries.

Representative Prompt
Design an execution attempt entity that records each attempt
made by an AI agent, including errors, results, timestamps,
tests and files changed.
Resulting Design

An ExecutionAttempt entity was introduced.

It records information such as:

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

This provides historical execution context.

13. Audit Trail
Prompt Purpose

AI assistance was used to design an audit system that is useful to both humans and agents.

Representative Prompt
How should an audit trail be designed for an AI-native project
management system so that users can understand everything an
agent did during task execution?
Resulting Design

An AuditLog entity was created.

Important events include:

TASK_CREATED
TASK_CLAIMED
TASK_STARTED
TASK_COMPLETED
TASK_FAILED
TASK_RETRY
HUMAN_REVIEW

Each event records the relevant task, agent, status, message and timestamp.

14. End-to-End Workflow Testing
Prompt Purpose

AI assistance was used to plan an end-to-end test of the system.

Representative Prompt
Give me an end-to-end testing workflow for the AI-native project
management system covering task creation, readiness, dependency,
agent claim, execution, completion, failure, retry and human review.
Testing Flow

The workflow was tested through REST APIs.

A successful flow was demonstrated:

Create Task
    |
    v
Check Readiness
    |
    v
Agent Claims Task
    |
    v
Start Execution
    |
    v
Complete Task
    |
    v
Dependency Becomes Available

A failure flow was also tested:

Task Execution
      |
      v
    FAILED
      |
      v
    RETRY
      |
      v
Maximum Retries
      |
      v
HUMAN REVIEW
15. Documentation Assistance
Prompt Purpose

AI was also used to help organize the required submission documentation.

Representative Prompt
What documentation should I prepare for the company assignment?
Create a structure for a PRD, architecture document, design thinking
document and AI prompt log.
Resulting Documents

The repository contains:

README.md
PRD.md
ARCHITECTURE.md
DESIGN_THINKING.md
AI-PROMPT-LOG.md

These documents describe the product requirements, architecture, design decisions and AI-assisted development process.

16. README Assistance
Prompt Purpose

AI assistance was used to organize the project README.

Representative Prompt
Create a professional README for my AI-native project management
system. Include the problem, architecture, features, setup,
API endpoints, workflows, technology stack and future enhancements.
Resulting Documentation

The README explains:

Project overview
Problem statement
Architecture
Features
REST endpoints
Task lifecycle
Dependency workflow
Failure and retry workflow
Human escalation
Audit trail
Technology stack
Setup instructions
Future improvements
17. Git and GitHub Assistance
Prompt Purpose

AI assistance was used to guide Git operations and repository organization.

Representative Prompt
I have completed the project locally. Give me step-by-step
commands to initialize Git, create commits, connect the GitHub
repository and push the project.
Git Workflow

The project was organized into multiple commits rather than one final squashed commit.

The development history includes separate commits for:

1. Implement AI-native project management system
2. Add product requirements document
3. Add architecture document
4. Add design thinking document

The prompt log itself is being added as a separate documentation step.

18. Debugging and Development Guidance

AI assistance was also used during development whenever implementation questions or errors occurred.

Typical assistance areas included:

Spring Boot project structure
Java class organization
JPA entities
Repository interfaces
REST controllers
Service-layer design
API testing
Git commands
Windows command-line operations
File naming and Markdown documents

AI suggestions were reviewed before being incorporated into the project.

19. AI Usage Boundaries

AI was used as a development assistant rather than as an autonomous replacement for project decisions.

The development process involved:

Requirement
    |
    v
AI Assistance
    |
    v
Review / Understanding
    |
    v
Implementation
    |
    v
Testing
    |
    v
Documentation

The AI-generated suggestions were adapted to the project's actual implementation and requirements.

20. Key Design Insights Obtained Through AI Assistance

The most important insights developed during the project were:

Insight 1 — Tickets Need Machine-Readable Context

An autonomous agent needs more than a short task description.

Insight 2 — Readiness Must Be Checked Before Execution

The system should detect missing information before assigning work to an agent.

Insight 3 — Dependencies Should Control Execution

Agents should not execute downstream tasks until upstream work is completed.

Insight 4 — Failures Need Structured Recovery

Retries should be controlled by an explicit retry policy.

Insight 5 — Human Escalation Should Be Explicit

The system should define conditions under which an agent must stop and request human intervention.

Insight 6 — Execution Context Must Be Preserved

Execution attempts provide context for future agents and human reviewers.

Insight 7 — Auditability Is Essential

Every important state transition should be recorded.

21. Future AI Integration

The current prototype uses AI-assisted development but does not directly connect the application to a live coding agent.

A future production implementation could integrate agents capable of:

Reading source repositories
Creating branches
Modifying code
Running tests
Analyzing failures
Creating pull requests
Responding to review feedback
Retrying failed work
Updating execution context

The platform could then become an orchestration layer for multiple autonomous development agents.

22. Prompt Links

The AI tool used during development was ChatGPT.

The development prompts were primarily conducted within the ChatGPT conversation used to build and document this project.

No fabricated external prompt URLs are included in this repository.

If the submission platform requires direct conversation links, the relevant ChatGPT conversation can be shared using the platform's supported conversation-sharing feature.

23. Summary

AI assistance contributed to the project in several areas:

Problem Understanding
        |
        v
Architecture Design
        |
        v
Task Contract Design
        |
        v
Backend Implementation
        |
        v
Testing and Debugging
        |
        v
Documentation
        |
        v
Git/GitHub Guidance