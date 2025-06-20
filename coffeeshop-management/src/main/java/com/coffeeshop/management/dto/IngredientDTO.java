package com.coffeeshop.management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {
    private Integer ingredientId;
    private String ingredientName;
    private String unit;
    private BigDecimal stockQuantity;
    private BigDecimal minStockLevel;
    private Integer categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}