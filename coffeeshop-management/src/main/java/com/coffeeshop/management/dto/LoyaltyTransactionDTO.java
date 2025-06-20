package com.coffeeshop.management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyTransactionDTO {
    private Integer transactionId;
    private Integer customerId;
    private Integer orderId;
    private Integer pointsChange;
    private String transactionType;
    private LocalDateTime transactionDate;
    private String description;
}