# Architecture Document

## AI-Native Project Management System

**Problem Statement:** 2 – AI-Native Project Management System  
**Role:** Full Stack Developer Intern  
**Technology:** Java 21, Spring Boot, Spring Data JPA, H2, Maven, REST APIs

---

# 1. Architecture Overview

The AI-Native Project Management System is designed for software teams that use autonomous AI agents to perform software-development tasks.

Instead of treating work as a traditional human-oriented Jira ticket, the system represents each task as an executable contract containing the context required by an AI agent.

The architecture separates:

- Task management
- Task readiness validation
- Dependency management
- Agent management
- Task execution
- Failure and retry handling
- Human escalation
- Execution attempt tracking
- Audit logging

The current implementation is a backend prototype using Spring Boot and H2 persistence.

---

# 2. High-Level Architecture

```text
                         +----------------------+
                         |       Client         |
                         |  Postman / Frontend  |
                         +----------+-----------+
                                    |
                                    | HTTP / REST
                                    v
                         +----------------------+
                         |   Spring Boot API    |
                         +----------+-----------+
                                    |
                +-------------------+-------------------+
                |                   |                   |
                v                   v                   v
        +--------------+    +--------------+    +--------------+
        | Task Module  |    | Agent Module |    | Execution    |
        |              |    |              |    | Module       |
        +------+-------+    +------+-------+    +------+-------+
               |                   |                   |
               +-------------------+-------------------+
                                   |
                                   v
                         +----------------------+
                         |    Service Layer     |
                         +----------+-----------+
                                    |
                                    v
                         +----------------------+
                         | Repository Layer     |
                         | Spring Data JPA      |
                         +----------+-----------+
                                    |
                                    v
                         +----------------------+
                         |     H2 Database      |
                         +----------------------+
