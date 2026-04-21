package com.polling.queue.management.repository;

import com.polling.queue.management.model.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AlertRepository extends MongoRepository<Alert, String> {
    List<Alert> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Alert> findByUserIdAndIsReadFalse(String userId);
    long countByUserIdAndIsReadFalse(String userId);
}

