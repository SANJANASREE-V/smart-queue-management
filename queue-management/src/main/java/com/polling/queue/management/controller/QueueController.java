package com.polling.queue.management.controller;

import com.polling.queue.management.dto.AlertSettingsRequest;
import com.polling.queue.management.dto.ApiResponse;
import com.polling.queue.management.model.Alert;
import com.polling.queue.management.service.AlertService;
import com.polling.queue.management.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
public class QueueController {

    private final AlertService alertService;
    private final JwtUtil jwtUtil;

    // GET /api/queue/alerts — get logged-in user's alerts
    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<Alert>>> getMyAlerts(
            @RequestHeader("Authorization") String authHeader) {
        String userId = extractUserId(authHeader);
        List<Alert> alerts = alertService.getUserAlerts(userId);
        return ResponseEntity.ok(
            ApiResponse.success("Alerts retrieved", alerts));
    }

    // GET /api/queue/alerts/unread-count
    @GetMapping("/alerts/unread-count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUnreadCount(
            @RequestHeader("Authorization") String authHeader) {
        String userId = extractUserId(authHeader);
        long count = alertService.getUnreadAlertCount(userId);
        return ResponseEntity.ok(
            ApiResponse.success("Unread count", Map.of("count", count)));
    }

    // PUT /api/queue/alerts/mark-read
    @PutMapping("/alerts/mark-read")
    public ResponseEntity<ApiResponse<Void>> markAllRead(
            @RequestHeader("Authorization") String authHeader) {
        String userId = extractUserId(authHeader);
        alertService.markAllRead(userId);
        return ResponseEntity.ok(
            ApiResponse.success("Alerts marked as read", null));
    }

    // PUT /api/queue/alert-settings
    @PutMapping("/alert-settings")
    public ResponseEntity<ApiResponse<Void>> updateAlertSettings(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody AlertSettingsRequest request) {
        String userId = extractUserId(authHeader);
        alertService.updateAlertSettings(userId, request);
        return ResponseEntity.ok(
            ApiResponse.success("Alert settings updated", null));
    }

    private String extractUserId(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }
}

