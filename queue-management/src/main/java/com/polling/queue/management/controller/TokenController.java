package com.polling.queue.management.controller;

import com.polling.queue.management.dto.ApiResponse;
import com.polling.queue.management.model.PollingBooth;
import com.polling.queue.management.repository.PollingBoothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final PollingBoothRepository boothRepository;

    // POST /api/tokens/generate
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateToken(
            @RequestBody Map<String, String> request) {

        String boothId = request.get("boothId");
        String userId  = request.get("userId");

        // Find booth
        PollingBooth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new RuntimeException("Booth not found"));

        // Generate token number
        int currentQueue  = booth.getCurrentQueueLength();
        int tokenNumber = currentQueue + 1;
        int peopleAhead  = currentQueue; // actual people in queue ahead
        int waitMinutes  = peopleAhead * 2;
        Map<String, Object> tokenData = new HashMap<>();

        tokenData.put("tokenNumber",  tokenNumber);
        tokenData.put("boothId",      boothId);
        tokenData.put("boothName",    booth.getBoothName());
        tokenData.put("boothNumber",  booth.getBoothNumber());
        tokenData.put("peopleAhead",  peopleAhead);
        tokenData.put("waitMinutes",  waitMinutes);
        tokenData.put("userId",       userId);

        return ResponseEntity.ok(
            ApiResponse.success("Token generated", tokenData));
    }
}