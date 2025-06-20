package com.coffeeshop.management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductIngredientDTO {
    private Integer productIngredientId;
    private Integer productId;
    private Integer ingredientId;
    private BigDecimal quantityNeeded;
}