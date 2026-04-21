package com.polling.queue.management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "polling_booths")
public class PollingBooth {

    @Id
    private String id;

    private String boothName;
    private String boothNumber;
    private String address;
    private String district;
    private String constituency;
    private int currentQueueLength;
    private int capacity; // max voters per hour
    private boolean isActive;
    private String crowdLevel; // "LOW", "MEDIUM", "HIGH"
    private int estimatedWaitMinutes;
    private String optimalVisitTime;
    private int peakQueueLevel;
    private int totalUpdates;
    private double averageQueueSize;
    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;
    private int lastTokenNumber = 0; 

    public int getLastTokenNumber() { return lastTokenNumber; }
    public void setLastTokenNumber(int n) { this.lastTokenNumber = n; }

    // Crowd level thresholds
    public void updateCrowdLevel() {
        double occupancyPercent = (double) currentQueueLength / capacity * 100;
        if (occupancyPercent <= 30) {
            this.crowdLevel = "LOW";
        } else if (occupancyPercent <= 70) {
            this.crowdLevel = "MEDIUM";
        } else {
            this.crowdLevel = "HIGH";
        }
    }
}

