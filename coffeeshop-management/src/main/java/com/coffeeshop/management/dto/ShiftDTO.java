package com.coffeeshop.management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDTO {
    private Integer shiftId;
    private Integer userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;
}