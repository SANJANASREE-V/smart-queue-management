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
@Document(collection = "alerts")
public class Alert {

    @Id
    private String id;

    private String userId;
    private String boothId;
    private String message;
    private boolean isRead;
    private int queueAtTimeOfAlert;
    private LocalDateTime createdAt;

    public Alert(String userId, String boothId, String message, int queueAtTimeOfAlert) {
        this.userId = userId;
        this.boothId = boothId;
        this.message = message;
        this.isRead = false;
        this.queueAtTimeOfAlert = queueAtTimeOfAlert;
        this.createdAt = LocalDateTime.now();
    }
}

