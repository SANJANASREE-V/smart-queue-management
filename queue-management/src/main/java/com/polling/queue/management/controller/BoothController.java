package com.polling.queue.management.controller;

import com.polling.queue.management.model.PollingBooth;
import com.polling.queue.management.model.QueueRecord;
import com.polling.queue.management.service.BoothService;
import com.polling.queue.management.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/booths")
@RequiredArgsConstructor
public class BoothController {

    private final BoothService boothService;

    // GET /api/booths — public: list all active booths
    @GetMapping
    public ResponseEntity<ApiResponse<List<PollingBooth>>> getAllBooths() {
        List<PollingBooth> booths = boothService.getAllActiveBooths();
        return ResponseEntity.ok(
            ApiResponse.success("Booths retrieved", booths));
    }

    // GET /api/booths/{id} — public: get booth details
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PollingBooth>> getBoothById(
            @PathVariable String id) {
        PollingBooth booth = boothService.getBoothById(id);
        return ResponseEntity.ok(
            ApiResponse.success("Booth retrieved", booth));
    }

    // GET /api/booths/{id}/history?hours=6
    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<QueueRecord>>> getBoothHistory(
            @PathVariable String id,
            @RequestParam(defaultValue = "6") int hours) {
        List<QueueRecord> history = boothService.getBoothHistory(id, hours);
        return ResponseEntity.ok(
            ApiResponse.success("Queue history retrieved", history));
    }

    // GET /api/booths/{id}/analytics
    @GetMapping("/{id}/analytics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBoothAnalytics(
            @PathVariable String id) {
        Map<String, Object> analytics = boothService.getBoothAnalytics(id);
        return ResponseEntity.ok(
            ApiResponse.success("Analytics retrieved", analytics));
    }
}

