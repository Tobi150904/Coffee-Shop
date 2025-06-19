package com.coffeeshop.management.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loyalty_transactions")
public class LoyaltyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "points_change", nullable = false)
    private Integer pointsChange;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @CreationTimestamp
    @Column(name = "transaction_date", updatable = false)
    private LocalDateTime transactionDate;

    @Column(length = 255)
    private String description;

    public enum TransactionType {
        EARN,
        REDEEM
    }
}


