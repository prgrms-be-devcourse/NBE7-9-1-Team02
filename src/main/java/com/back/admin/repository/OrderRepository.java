package com.back.admin.repository;

import com.back.admin.domain.model.Order;
import com.back.admin.domain.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    // 주문 상태별로 조회
    List<Order> findByStatus(OrderStatus status);
}
