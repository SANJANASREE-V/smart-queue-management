package com.polling.queue.management.service;

import com.polling.queue.management.model.PollingBooth;
import com.polling.queue.management.model.QueueRecord;
import com.polling.queue.management.repository.PollingBoothRepository;
import com.polling.queue.management.repository.QueueRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final PollingBoothRepository boothRepository;
    private final QueueRecordRepository queueRecordRepository;
    private final AlertService alertService;
    private final SimpMessagingTemplate messagingTemplate;

    private static final int MINUTES_PER_VOTER = 2; // avg time per voter

    public PollingBooth updateQueue(String boothId, int newQueueLength, String adminId) {
        PollingBooth booth = boothRepository.findById(boothId)
            .orElseThrow(() -> new RuntimeException("Booth not found: " + boothId));

        int previousQueue = booth.getCurrentQueueLength();
        booth.setCurrentQueueLength(newQueueLength);
        booth.updateCrowdLevel();
        booth.setEstimatedWaitMinutes(calculateWaitTime(newQueueLength));
        booth.setOptimalVisitTime(calculateOptimalTime(booth));
        booth.setLastUpdated(LocalDateTime.now());

        // Update analytics
        if (newQueueLength > booth.getPeakQueueLevel()) {
            booth.setPeakQueueLevel(newQueueLength);
        }
        booth.setTotalUpdates(booth.getTotalUpdates() + 1);
        updateAverageQueueSize(booth, newQueueLength);

        PollingBooth updatedBooth = boothRepository.save(booth);

        // Save historical record
        QueueRecord record = new QueueRecord(
            boothId, newQueueLength,
            booth.getCrowdLevel(),
            booth.getEstimatedWaitMinutes(),
            adminId
        );
        // Update crowd level based on new queue length
        if (newQueueLength < 15) {
            booth.setCrowdLevel("LOW");
            booth.setEstimatedWaitMinutes(newQueueLength * 2);
        } else if (newQueueLength < 30) {
        booth.setCrowdLevel("MEDIUM");
        booth.setEstimatedWaitMinutes(newQueueLength * 2);
        } else {
            booth.setCrowdLevel("HIGH");
            booth.setEstimatedWaitMinutes(newQueueLength * 2);
        }

        // Update optimal visit time
        booth.setOptimalVisitTime(calculateOptimalTime(booth));

        record.setBoothNumber(booth.getBoothNumber());
        queueRecordRepository.save(record);
        record.setBoothNumber(booth.getBoothNumber()); 
        queueRecordRepository.save(record);

        // Send WebSocket broadcast to all connected clients
        messagingTemplate.convertAndSend("/topic/queue/" + boothId, updatedBooth);

        // Trigger alerts if queue reduced
        if (newQueueLength < previousQueue) {
            alertService.triggerQueueAlerts(boothId, newQueueLength, updatedBooth);
        }

        return updatedBooth;
    }

    public int calculateWaitTime(int queueLength) {
        return queueLength * MINUTES_PER_VOTER;
    }

    private String calculateOptimalTime(PollingBooth booth) {
        // Suggest visiting when queue is low
        String crowdLevel = booth.getCrowdLevel();
        switch (crowdLevel) {
            case "LOW":
                return "Now is a great time to vote!";
            case "MEDIUM":
                return "Try visiting in 1-2 hours for shorter wait.";
            case "HIGH":
                return "Recommended: Visit early morning or late afternoon.";
            default:
                return "Visit during off-peak hours for best experience.";
        }
    }

    private void updateAverageQueueSize(PollingBooth booth, int newQueueLength) {
        double currentAvg = booth.getAverageQueueSize();
        int totalUpdates = booth.getTotalUpdates();
        double newAvg = ((currentAvg * (totalUpdates - 1)) + newQueueLength) / totalUpdates;
        booth.setAverageQueueSize(Math.round(newAvg * 100.0) / 100.0);
    }
}

