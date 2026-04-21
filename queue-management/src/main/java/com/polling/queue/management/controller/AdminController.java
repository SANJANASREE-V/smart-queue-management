package com.polling.queue.management.controller;

import com.polling.queue.management.dto.ApiResponse;
import com.polling.queue.management.dto.BoothCreateRequest;
import com.polling.queue.management.dto.QueueUpdateRequest;
import com.polling.queue.management.dto.RegisterRequest;
import com.polling.queue.management.model.PollingBooth;
import com.polling.queue.management.model.User;
import com.polling.queue.management.service.AnalyticsService;
import com.polling.queue.management.service.AuthService;
import com.polling.queue.management.service.BoothService;
import com.polling.queue.management.service.QueueService;
import com.polling.queue.management.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final BoothService boothService;
    private final QueueService queueService;
    private final AuthService authService;
    private final AnalyticsService analyticsService;
    private final JwtUtil jwtUtil;

    // POST /api/admin/booths — create a new booth
    @PostMapping("/booths")
    public ResponseEntity<ApiResponse<PollingBooth>> createBooth(
            @Valid @RequestBody BoothCreateRequest request) {
        PollingBooth booth = boothService.createBooth(request);
        return ResponseEntity.ok(
            ApiResponse.success("Booth created successfully", booth));
    }

    // PUT /api/admin/queue/update — update queue length
    @PutMapping("/queue/update")
    public ResponseEntity<ApiResponse<PollingBooth>> updateQueue(
            @Valid @RequestBody QueueUpdateRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String adminId = extractUserId(authHeader);
        PollingBooth updated = queueService.updateQueue(
            request.getBoothId(), request.getQueueLength(), adminId);
        return ResponseEntity.ok(
            ApiResponse.success("Queue updated successfully", updated));
    }

    // PUT /api/admin/booths/{id}/toggle — activate/deactivate booth
    @PutMapping("/booths/{id}/toggle")
    public ResponseEntity<ApiResponse<Void>> toggleBooth(@PathVariable String id) {
        boothService.toggleBoothStatus(id);
        return ResponseEntity.ok(
            ApiResponse.success("Booth status toggled", null));
    }

    // GET /api/admin/analytics/overview — system-wide stats
    @GetMapping("/analytics/overview")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOverview() {
        Map<String, Object> overview = analyticsService.getSystemOverview();
        return ResponseEntity.ok(
            ApiResponse.success("System overview", overview));
    }

    // GET /api/admin/analytics/trend/{boothId}?hours=6
    @GetMapping("/analytics/trend/{boothId}")
    public ResponseEntity<ApiResponse<?>> getBoothTrend(
            @PathVariable String boothId,
            @RequestParam(defaultValue = "6") int hours) {
        return ResponseEntity.ok(
            ApiResponse.success("Trend data",
                analyticsService.getBoothTrend(boothId, hours)));
    }

    // POST /api/admin/create-admin — create another admin account
    @PostMapping("/create-admin")
    public ResponseEntity<ApiResponse<User>> createAdmin(
            @Valid @RequestBody RegisterRequest request) {
        User admin = authService.registerAdmin(request);
        admin.setPassword(null); // Never return password
        return ResponseEntity.ok(
            ApiResponse.success("Admin created", admin));
    }

    private String extractUserId(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }
}

