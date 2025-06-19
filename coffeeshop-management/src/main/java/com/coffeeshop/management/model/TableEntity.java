package com.coffeeshop.management.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tables")
public class TableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tableId;

    @Column(name = "table_number", unique = true, nullable = false, length = 10)
    private String tableNumber;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('AVAILABLE', 'OCCUPIED', 'RESERVED', 'CLEANING') DEFAULT 'AVAILABLE'")
    private TableStatus status;

    public enum TableStatus {
        AVAILABLE,
        OCCUPIED,
        RESERVED,
        CLEANING
    }
}


