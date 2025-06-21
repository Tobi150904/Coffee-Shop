package com.coffeeshop.management.repository;

import com.coffeeshop.management.model.LoyaltyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, Integer> {
    List<LoyaltyTransaction> findByCustomer_CustomerId(Integer customerId);
    List<LoyaltyTransaction> findByOrder_OrderId(Integer orderId);
}

