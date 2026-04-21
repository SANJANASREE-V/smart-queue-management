package com.polling.queue.management.controller;

import com.polling.queue.management.dto.ApiResponse;
import com.polling.queue.management.model.Alert;
import com.polling.queue.management.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    // POST /api/alerts — save alert
    @PostMapping
    public ResponseEntity<ApiResponse<Alert>> createAlert(
            @RequestBody Alert alert) {
        Alert saved = alertService.saveAlert(alert);
        return ResponseEntity.ok(
            ApiResponse.success("Alert saved successfully", saved));
    }

    // GET /api/alerts/{userId} — get alerts for a user
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> getUserAlerts(
            @PathVariable String userId) {
        return ResponseEntity.ok(
            ApiResponse.success("Alerts retrieved",
                alertService.getAlertsByUserId(userId)));
    }

    // DELETE /api/alerts/{id} — delete alert
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteAlert(
            @PathVariable String id) {
        alertService.deleteAlert(id);
        return ResponseEntity.ok(
            ApiResponse.success("Alert deleted", null));
    }
}