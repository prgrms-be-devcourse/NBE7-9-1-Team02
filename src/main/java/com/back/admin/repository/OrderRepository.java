package com.back.admin.repository;

import com.back.admin.domain.model.Order;
import com.back.admin.domain.model.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    // 주문 상태별로 조회
    List<Order> findByStatus(OrderStatus status);

    // 상세조회 시 N+1 방지: OrderProduct, 그리고 OrderProduct -> Product 같이 fetch
    @EntityGraph(attributePaths = {"orderProducts", "orderProducts.product"})
    Optional<Order> findWithProductsById(Integer id);
}
