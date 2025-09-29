package com.back.domain.order.repository;

import com.back.domain.order.entity.OrderStatus;
import com.back.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // 주문 상태별 조회
    List<Order> findByStatus(OrderStatus status);

    // 상세조회(fetch join으로 N+1 방지)
    @EntityGraph(attributePaths = {"orderProducts", "orderProducts.product"})
    Optional<Order> findWithProductsById(Integer id);

    // 상태별 페이징 조회
    //Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}
