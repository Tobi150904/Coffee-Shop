package com.coffeeshop.management.service;

import com.coffeeshop.management.dto.OrderDTO;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO) throws ResourceNotFoundException;
    List<OrderDTO> getAllOrders();
    OrderDTO getOrderById(Integer id) throws ResourceNotFoundException;
    OrderDTO updateOrder(Integer id, OrderDTO orderDTO) throws ResourceNotFoundException;
    void deleteOrder(Integer id) throws ResourceNotFoundException;
    List<OrderDTO> getOrdersByStatus(String status);
    List<OrderDTO> getOrdersByOrderType(String orderType);
    List<OrderDTO> getOrdersByCustomerId(Integer customerId) throws ResourceNotFoundException;
    List<OrderDTO> getOrdersByUserId(Integer userId) throws ResourceNotFoundException;
}
