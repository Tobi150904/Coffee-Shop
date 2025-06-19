package com.coffeeshop.management.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(name = "category_name", unique = true, nullable = false, length = 100)
    private String categoryName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType type;

    public enum CategoryType {
        PRODUCT,
        INGREDIENT
    }
}


