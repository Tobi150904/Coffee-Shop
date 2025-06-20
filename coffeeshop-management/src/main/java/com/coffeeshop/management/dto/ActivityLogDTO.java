package com.coffeeshop.management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogDTO {
    private Integer logId;
    private Integer userId;
    private String action;
    private String entityType;
    private Integer entityId;
    private LocalDateTime timestamp;
    private String details;
}