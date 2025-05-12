package com.example.testing.domain.order.entity;

import com.example.testing.domain.product.entity.Product;
import com.example.testing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private double totalAmount;


    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void updateTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}