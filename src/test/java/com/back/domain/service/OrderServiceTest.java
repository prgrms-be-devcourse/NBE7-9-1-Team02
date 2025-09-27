package com.back.domain.service;

import com.back.domain.order.entity.Orders;
import com.back.domain.order.entity.OrderStatus;
import com.back.domain.order.repository.OrdersRepository;
import com.back.domain.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrdersRepository orderRepository;

    @Test
    @DisplayName("14시에 배송중(SHIPPED) 주문이 배달완료(DELIVERED)로 바뀌는지")
    void testDeliverAllShippedOrders() {
        // 1. 배송중 상태의 주문 생성
        Orders order = new Orders();
        order.setEmail("delivery@test.com");
        order.setTotalPrice(20000L);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.SHIPPED);
        order.setShippedAt(OffsetDateTime.now(ZoneOffset.ofHours(9))); // 서울 기준
        orderRepository.save(order);

        // 2. 14시 스케줄러 실행 (테스트에서는 직접 호출)
        orderService.deliverAllShippedOrders();

        // 3. DB에서 다시 조회
        Orders updatedOrder = orderRepository.findById(order.getId())
                .orElseThrow();

        // 4. 상태 확인
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }
}
