package com.example.ainativeprojectmanagement.controller;

import com.example.ainativeprojectmanagement.model.Task;
import com.example.ainativeprojectmanagement.service.HumanReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/human-review")
public class HumanReviewController {

    private final HumanReviewService humanReviewService;

    public HumanReviewController(
            HumanReviewService humanReviewService) {

        this.humanReviewService = humanReviewService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getHumanReviewTasks() {

        return ResponseEntity.ok(
                humanReviewService.getHumanReviewTasks()
        );
    }

    @PostMapping("/tasks/{taskId}/approve-retry")
    public ResponseEntity<Map<String, Object>> approveRetry(
            @PathVariable Long taskId) {

        String message =
                humanReviewService.approveRetry(taskId);

        return ResponseEntity.ok(
                Map.of(
                        "taskId", taskId,
                        "message", message
                )
        );
    }
}