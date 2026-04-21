package com.polling.queue.management.dto;

import lombok.Data;

@Data
public class AlertSettingsRequest {
    private boolean alertEnabled;
    private int alertThreshold;
}

