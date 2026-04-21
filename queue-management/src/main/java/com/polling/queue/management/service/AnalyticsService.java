package com.polling.queue.management.service;

import com.polling.queue.management.model.PollingBooth;
import com.polling.queue.management.model.QueueRecord;
import com.polling.queue.management.repository.PollingBoothRepository;
import com.polling.queue.management.repository.QueueRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final PollingBoothRepository boothRepository;
    private final QueueRecordRepository queueRecordRepository;

    public Map<String, Object> getSystemOverview() {
        List<PollingBooth> allBooths = boothRepository.findAll();

        long totalBooths = allBooths.size();
        long activeBooths = allBooths.stream().filter(PollingBooth::isActive).count();
        int totalVotersInQueue = allBooths.stream()
            .mapToInt(PollingBooth::getCurrentQueueLength).sum();
        long highCrowdBooths = allBooths.stream()
            .filter(b -> "HIGH".equals(b.getCrowdLevel())).count();

        Map<String, Object> overview = new HashMap<>();
        overview.put("totalBooths", totalBooths);
        overview.put("activeBooths", activeBooths);
        overview.put("totalVotersInQueue", totalVotersInQueue);
        overview.put("highCrowdBooths", highCrowdBooths);
        overview.put("timestamp", LocalDateTime.now());

        return overview;
    }

    public List<QueueRecord> getBoothTrend(String boothId, int hours) {
        LocalDateTime from = LocalDateTime.now().minusHours(hours);
        return queueRecordRepository
            .findByBoothIdAndTimestampBetweenOrderByTimestampAsc(
                boothId, from, LocalDateTime.now());
    }
}

