package com.example.ainativeprojectmanagement.repository;

import com.example.ainativeprojectmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}