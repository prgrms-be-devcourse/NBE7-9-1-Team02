package com.back.admin.service;

import com.back.admin.domain.model.Order;
import com.back.admin.domain.model.OrderProduct;
import com.back.admin.domain.model.OrderStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private List<Order> orders = new ArrayList<>();

    public List<Order> findAllOrders() {
        return orders;
    }

    public List<Order> findOrdersByStatus(OrderStatus status) {
        return orders.stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());
    }
}
