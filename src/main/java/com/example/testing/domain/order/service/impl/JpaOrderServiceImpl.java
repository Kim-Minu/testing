package com.example.testing.domain.order.service.impl;

import com.example.testing.domain.order.entity.Order;
import com.example.testing.domain.order.entity.OrderStatus;
import com.example.testing.domain.order.repository.OrderRepository;
import com.example.testing.domain.order.service.OrderService;
import com.example.testing.domain.product.entity.Product;
import com.example.testing.domain.product.repository.ProductRepository;
import com.example.testing.domain.user.entity.User;
import com.example.testing.domain.user.repository.UserRepository;
import com.example.testing.global.exception.EntityNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class JpaOrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public JpaOrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id " + id));
    }

    @Override
    @Transactional
    public Order createOrder(Long userId, Long productId, int quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id " + productId));

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product id " + productId);
        }

        product.updateStock(quantity);

        productRepository.save(product);

        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .user(user)
                .product(product)
                .quantity(quantity)
                .status(OrderStatus.PENDING)
                .totalAmount(product.getPrice() * quantity)
                .build();

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order cancelOrder(Long id) {
        Order order = getOrderById(id);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Only pending orders can be canceled.");
        }

        order.updateStatus(OrderStatus.CANCELED);

        orderRepository.save(order);

        // 재고 복구
        Product product = order.getProduct();
        product.updateStock(product.getStock() + order.getQuantity());

        productRepository.save(product);

        return order;
    }

    @Override
    @Transactional
    public Order updateOrderQuantity(Long id, int newQuantity) {
        Order order = getOrderById(id);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Only pending orders can be updated.");
        }

        Product product = order.getProduct();
        int difference = newQuantity - order.getQuantity();

        if (difference > 0 && product.getStock() < difference) {
            throw new IllegalArgumentException("Insufficient stock to increase quantity.");
        }

        product.updateStock(product.getStock() - difference);
        productRepository.save(product);

        order.updateQuantity(newQuantity);

        order.updateTotalAmount(product.getPrice() * newQuantity);

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));

        return orderRepository.findByUser(user);
    }

    @Override
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }

    @Override
    public Double calculateTotalAmount(Long id) {
        Order order = getOrderById(id);
        return order.getTotalAmount();
    }
}