package com.coffeeshop.management.service.impl;

import com.coffeeshop.management.dto.OrderDetailDTO;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import com.coffeeshop.management.model.OrderDetail;
import com.coffeeshop.management.repository.OrderDetailRepository;
import com.coffeeshop.management.service.OrderDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetailDTO createOrderDetail(OrderDetailDTO dto) {
        OrderDetail entity = new OrderDetail();
        BeanUtils.copyProperties(dto, entity);
        OrderDetail saved = orderDetailRepository.save(entity);
        return convertToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailDTO> getAllOrderDetails() {
        return orderDetailRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailDTO getOrderDetailById(Integer id) throws ResourceNotFoundException {
        return orderDetailRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id: " + id));
    }

    @Override
    public void deleteOrderDetail(Integer id) throws ResourceNotFoundException {
        if (!orderDetailRepository.existsById(id)) {
            throw new ResourceNotFoundException("OrderDetail not found with id: " + id);
        }
        orderDetailRepository.deleteById(id);
    }

    private OrderDetailDTO convertToDto(OrderDetail entity) {
        OrderDetailDTO dto = new OrderDetailDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
