package com.back.domain.order.repository;

import com.back.admin.domain.model.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    // 주문 상태별 조회
    List<Orders> findByStatus(OrderStatus status);

    // 상세조회(fetch join으로 N+1 방지)
    @EntityGraph(attributePaths = {"orderProducts", "orderProducts.product"})
    Optional<Orders> findWithProductsById(Integer id);
}
