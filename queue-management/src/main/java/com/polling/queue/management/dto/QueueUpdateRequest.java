package com.polling.queue.management.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QueueUpdateRequest {

    @NotBlank(message = "Booth ID is required")
    private String boothId;

    @Min(value = 0, message = "Queue length cannot be negative")
    private int queueLength;
}

