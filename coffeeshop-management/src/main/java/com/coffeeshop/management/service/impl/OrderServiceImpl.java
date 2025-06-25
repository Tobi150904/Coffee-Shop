package com.coffeeshop.management.service.impl;

import com.coffeeshop.management.dto.OrderDTO;
import com.coffeeshop.management.dto.OrderDetailDTO;
import com.coffeeshop.management.model.*;
import com.coffeeshop.management.repository.*;
import com.coffeeshop.management.service.IngredientService;
import com.coffeeshop.management.service.OrderService;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductIngredientRepository productIngredientRepository;
    private final IngredientService ingredientService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                          OrderDetailRepository orderDetailRepository,
                          CustomerRepository customerRepository,
                          TableRepository tableRepository,
                          UserRepository userRepository,
                          ProductRepository productRepository,
                          ProductIngredientRepository productIngredientRepository,
                          IngredientService ingredientService) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.customerRepository = customerRepository;
        this.tableRepository = tableRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productIngredientRepository = productIngredientRepository;
        this.ingredientService = ingredientService;
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) throws ResourceNotFoundException {
        validateOrderDTO(orderDTO);
        
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        
        // Set relationships
        if (orderDTO.getCustomerId() != null) {
            Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + orderDTO.getCustomerId()));
            order.setCustomer(customer);
        }
        
        if (orderDTO.getTableId() != null) {
            TableEntity table = tableRepository.findById(orderDTO.getTableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + orderDTO.getTableId()));
            order.setTable(table);
        }
        
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + orderDTO.getUserId()));
        order.setUser(user);
        
        // Calculate amounts
        BigDecimal totalAmount = calculateTotalAmount(orderDTO);
        order.setTotalAmount(totalAmount);
        order.setFinalAmount(totalAmount.subtract(
            orderDTO.getDiscountAmount() != null ? orderDTO.getDiscountAmount() : BigDecimal.ZERO
        ));

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Save order details
        saveOrderDetails(orderDTO, savedOrder);

        return convertToDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Integer id) throws ResourceNotFoundException {
        return orderRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Override
    public OrderDTO updateOrder(Integer id, OrderDTO orderDTO) throws ResourceNotFoundException {
        validateOrderDTO(orderDTO);
        
        return orderRepository.findById(id).map(existingOrder -> {
            BeanUtils.copyProperties(orderDTO, existingOrder, "orderId", "orderDate");
            
            // Update relationships
            if (orderDTO.getCustomerId() != null) {
                Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + orderDTO.getCustomerId()));
                existingOrder.setCustomer(customer);
            } else {
                existingOrder.setCustomer(null);
            }
            
            if (orderDTO.getTableId() != null) {
                TableEntity table = tableRepository.findById(orderDTO.getTableId())
                        .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + orderDTO.getTableId()));
                existingOrder.setTable(table);
            } else {
                existingOrder.setTable(null);
            }
            
            User user = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + orderDTO.getUserId()));
            existingOrder.setUser(user);
            
            // Update amounts
            BigDecimal totalAmount = calculateTotalAmount(orderDTO);
            existingOrder.setTotalAmount(totalAmount);
            existingOrder.setFinalAmount(totalAmount.subtract(
                orderDTO.getDiscountAmount() != null ? orderDTO.getDiscountAmount() : BigDecimal.ZERO
            ));
            
            // Update order details
            orderDetailRepository.deleteAll(existingOrder.getOrderDetails());
            existingOrder.getOrderDetails().clear();
            saveOrderDetails(orderDTO, existingOrder);
            
            Order updatedOrder = orderRepository.save(existingOrder);
            return convertToDto(updatedOrder);
        }).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Override
    public void deleteOrder(Integer id) throws ResourceNotFoundException {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(Order.OrderStatus.valueOf(status.toUpperCase())).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByOrderType(String orderType) {
        return orderRepository.findByOrderType(Order.OrderType.valueOf(orderType.toUpperCase())).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByCustomerId(Integer customerId) throws ResourceNotFoundException {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }
        return orderRepository.findByCustomer_CustomerId(customerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByUserId(Integer userId) throws ResourceNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return orderRepository.findByUser_UserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BigDecimal calculateTotalAmount(OrderDTO orderDTO) throws ResourceNotFoundException {
        return orderDTO.getOrderDetails().stream()
                .map(detail -> {
                    Product product = productRepository.findById(detail.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + detail.getProductId()));
                    return product.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void saveOrderDetails(OrderDTO orderDTO, Order order) throws ResourceNotFoundException {
        for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(detailDTO, orderDetail);
            orderDetail.setOrder(order);
            
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + detailDTO.getProductId()));
            orderDetail.setProduct(product);
            orderDetail.setPriceAtOrder(product.getPrice());
            
            orderDetailRepository.save(orderDetail);
            
            // Update inventory
            updateInventory(detailDTO);
        }
    }

    private void updateInventory(OrderDetailDTO detailDTO) throws ResourceNotFoundException {
        List<ProductIngredient> ingredients = productIngredientRepository.findByProductId(detailDTO.getProductId());
        for (ProductIngredient pi : ingredients) {
            BigDecimal quantityUsed = pi.getQuantityNeeded().multiply(BigDecimal.valueOf(detailDTO.getQuantity()));
            ingredientService.updateStockQuantity(pi.getIngredient().getIngredientId(), quantityUsed.negate());
        }
    }

    private OrderDTO convertToDto(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        
        if (order.getCustomer() != null) {
            orderDTO.setCustomerId(order.getCustomer().getCustomerId());
        }
        if (order.getTable() != null) {
            orderDTO.setTableId(order.getTable().getTableId());
        }
        if (order.getUser() != null) {
            orderDTO.setUserId(order.getUser().getUserId());
        }
        
        orderDTO.setStatus(order.getStatus().name());
        orderDTO.setOrderType(order.getOrderType().name());
        
        if (order.getOrderDetails() != null) {
            orderDTO.setOrderDetails(order.getOrderDetails().stream()
                    .map(this::convertOrderDetailToDto)
                    .collect(Collectors.toList()));
        }
        
        return orderDTO;
    }

    private OrderDetailDTO convertOrderDetailToDto(OrderDetail orderDetail) {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        BeanUtils.copyProperties(orderDetail, orderDetailDTO);
        orderDetailDTO.setOrderId(orderDetail.getOrder().getOrderId());
        orderDetailDTO.setProductId(orderDetail.getProduct().getProductId());
        return orderDetailDTO;
    }

    private void validateOrderDTO(OrderDTO orderDTO) {
        if (orderDTO.getOrderDetails() == null || orderDTO.getOrderDetails().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one order detail");
        }
        if (orderDTO.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }
}
