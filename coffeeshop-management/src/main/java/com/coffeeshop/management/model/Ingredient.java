package com.coffeeshop.management.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ingredientId;

    @Column(name = "ingredient_name", unique = true, nullable = false, length = 100)
    private String ingredientName;

    @Column(nullable = false, length = 20)
    private String unit;

    @Column(name = "stock_quantity", precision = 10, scale = 2, columnDefinition = "DECIMAL(10, 2) DEFAULT 0.0")
    private BigDecimal stockQuantity;

    @Column(name = "min_stock_level", precision = 10, scale = 2, columnDefinition = "DECIMAL(10, 2) DEFAULT 0.0")
    private BigDecimal minStockLevel;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


