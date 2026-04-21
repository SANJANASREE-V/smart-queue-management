package com.polling.queue.management.repository;



import com.polling.queue.management.model.PollingBooth;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PollingBoothRepository extends MongoRepository<PollingBooth, String> {
    List<PollingBooth> findByIsActiveTrue();
    Optional<PollingBooth> findByBoothNumber(String boothNumber);
    List<PollingBooth> findByDistrict(String district);
    List<PollingBooth> findByConstituency(String constituency);
    List<PollingBooth> findByActiveTrue();
}

