package com.example.testing.domain.order.service;

import com.example.testing.domain.order.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    Order createOrder(Long userId, Long productId, int quantity);
    Order cancelOrder(Long id);
    Order updateOrderQuantity(Long id, int newQuantity);
    List<Order> getOrdersByUserId(Long userId);
    List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    Double calculateTotalAmount(Long id);
}