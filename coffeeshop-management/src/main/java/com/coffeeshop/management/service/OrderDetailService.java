package com.coffeeshop.management.service;

import com.coffeeshop.management.dto.OrderDetailDTO;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import java.util.List;

public interface OrderDetailService {
    OrderDetailDTO createOrderDetail(OrderDetailDTO dto);
    List<OrderDetailDTO> getAllOrderDetails();
    OrderDetailDTO getOrderDetailById(Integer id) throws ResourceNotFoundException;
    void deleteOrderDetail(Integer id) throws ResourceNotFoundException;
}
