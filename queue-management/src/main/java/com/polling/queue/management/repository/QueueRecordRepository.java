package com.polling.queue.management.repository;

import com.polling.queue.management.model.QueueRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface QueueRecordRepository extends MongoRepository<QueueRecord, String> {
    List<QueueRecord> findByBoothIdOrderByTimestampDesc(String boothId);
    List<QueueRecord> findByBoothIdAndTimestampBetweenOrderByTimestampAsc(
            String boothId, LocalDateTime start, LocalDateTime end);
    List<QueueRecord> findTop24ByBoothIdOrderByTimestampDesc(String boothId);
    long countByBoothId(String boothId);
}
