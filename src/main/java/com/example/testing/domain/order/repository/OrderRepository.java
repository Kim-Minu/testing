package com.example.testing.domain.order.repository;

import com.example.testing.domain.order.entity.Order;
import com.example.testing.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}