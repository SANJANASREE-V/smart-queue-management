package com.polling.queue.management.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BoothCreateRequest {

    @NotBlank(message = "Booth name is required")
    private String boothName;

    @NotBlank(message = "Booth number is required")
    private String boothNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "Constituency is required")
    private String constituency;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;
}

