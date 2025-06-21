package com.coffeeshop.management.repository;

import com.coffeeshop.management.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByStatus(Order.OrderStatus status);
    List<Order> findByOrderType(Order.OrderType orderType);
    List<Order> findByCustomer_CustomerId(Integer customerId);
    List<Order> findByUser_UserId(Integer userId);
}