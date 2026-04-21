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
@Document(collection = "queue_records")
public class QueueRecord {

    @Id
    private String id;

    private String boothId;
    private String boothNumber; 
    private int queueLength;
    private String crowdLevel;
    private int estimatedWaitMinutes;
    private String updatedByAdminId;
    private LocalDateTime timestamp;

    public QueueRecord(String boothId, int queueLength,
                       String crowdLevel, int estimatedWaitMinutes,
                       String updatedByAdminId) {
        this.boothId = boothId;
        this.queueLength = queueLength;
        this.crowdLevel = crowdLevel;
        this.estimatedWaitMinutes = estimatedWaitMinutes;
        this.updatedByAdminId = updatedByAdminId;
        this.timestamp = LocalDateTime.now();
    }
    public String getBoothNumber() { return boothNumber; }
    public void setBoothNumber(String boothNumber) { 
        this.boothNumber = boothNumber; 
    }      
}
