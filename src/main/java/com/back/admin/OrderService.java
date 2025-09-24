package com.back.admin;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private List<Order> orders = new ArrayList<>();

    @PostConstruct
    public void init() {
        orders.add(new Order(1, "user1@example.com", LocalDateTime.now(), OrderStatus.PAID,
                List.of(new OrderProduct(101, "노트북", 1_000_000, 1))));
        orders.add(new Order(2, "user2@example.com", LocalDateTime.now(), OrderStatus.DELIVERING,
                List.of(new OrderProduct(102, "마우스", 50000, 2))));
        //orders.add(new Order(3, "user3@example.com", LocalDateTime.now(), OrderStatus.DELIVERED,
        //        List.of(new OrderProduct(103, "키보드", 150_000, 1))));
    }

    public List<Order> findAllOrders() {
        return orders;
    }

    public List<Order> findOrdersByStatus(OrderStatus status) {
        return orders.stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());
    }
}
