package com.polling.queue.management.service;

import com.polling.queue.management.dto.BoothCreateRequest;
import com.polling.queue.management.model.PollingBooth;
import com.polling.queue.management.model.QueueRecord;
import com.polling.queue.management.repository.PollingBoothRepository;
import com.polling.queue.management.repository.QueueRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoothService {

    private final PollingBoothRepository boothRepository;
    private final QueueRecordRepository queueRecordRepository;

    public PollingBooth createBooth(BoothCreateRequest request) {
        PollingBooth booth = new PollingBooth();
        booth.setBoothName(request.getBoothName());
        booth.setBoothNumber(request.getBoothNumber());
        booth.setAddress(request.getAddress());
        booth.setDistrict(request.getDistrict());
        booth.setConstituency(request.getConstituency());
        booth.setCapacity(request.getCapacity());
        booth.setCurrentQueueLength(0);
        booth.setActive(true);
        booth.setCrowdLevel("LOW");
        booth.setEstimatedWaitMinutes(0);
        booth.setOptimalVisitTime("Now is a great time to vote!");
        booth.setPeakQueueLevel(0);
        booth.setTotalUpdates(0);
        booth.setAverageQueueSize(0.0);
        booth.setLastUpdated(LocalDateTime.now());
        booth.setCreatedAt(LocalDateTime.now());
        return boothRepository.save(booth);
    }

    public List<PollingBooth> getAllActiveBooths() {
    return boothRepository.findByIsActiveTrue(); // ← filter condition
    }

    public PollingBooth getBoothById(String boothId) {
        return boothRepository.findById(boothId)
            .orElseThrow(() -> new RuntimeException("Booth not found"));
    }

    public List<QueueRecord> getBoothHistory(String boothId, int hours) {
        LocalDateTime from = LocalDateTime.now().minusHours(hours);
        LocalDateTime to = LocalDateTime.now();
        return queueRecordRepository
            .findByBoothIdAndTimestampBetweenOrderByTimestampAsc(boothId, from, to);
    }

    public Map<String, Object> getBoothAnalytics(String boothId) {
        PollingBooth booth = getBoothById(boothId);
        List<QueueRecord> last24 =
            queueRecordRepository.findTop24ByBoothIdOrderByTimestampDesc(boothId);

        return Map.of(
            "boothId", boothId,
            "boothName", booth.getBoothName(),
            "currentQueue", booth.getCurrentQueueLength(),
            "crowdLevel", booth.getCrowdLevel(),
            "peakQueueLevel", booth.getPeakQueueLevel(),
            "totalUpdates", booth.getTotalUpdates(),
            "averageQueueSize", booth.getAverageQueueSize(),
            "recentHistory", last24
        );
    }

    public void toggleBoothStatus(String boothId) {
        PollingBooth booth = getBoothById(boothId);
        booth.setActive(!booth.isActive());
        boothRepository.save(booth);
    }
}

