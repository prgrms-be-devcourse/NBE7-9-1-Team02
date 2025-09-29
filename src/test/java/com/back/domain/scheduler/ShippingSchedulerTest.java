package com.back.domain.scheduler;

import com.back.domain.order.entity.Orders;
import com.back.domain.order.entity.OrderStatus;
import com.back.domain.order.repository.OrdersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ShippingSchedulerTest {

    @Autowired
    private OrdersRepository orderRepository;

    @Autowired
    private ShippingScheduler shippingScheduler;

    @Test
    void testDeliverShippedOrders() {
        // given - 배송중인 주문 저장
        Orders order = new Orders();
        order.setEmail("scheduler@test.com");
        order.setStatus(OrderStatus.SHIPPED);
        order.setTotalPrice(20000L);
        order.setOrderDate(LocalDateTime.now());
        order.setShippedAt(OffsetDateTime.now());
        order.setAddress("아아시 야야구");
        order.setZipcode(12345);
        order.setCustomerName("dkdd");
        orderRepository.save(order);

        // when - 스케줄러 메서드 직접 실행
        shippingScheduler.deliverShipped();

        // then - 상태가 DELIVERED로 변경됐는지 확인
        Orders found = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    void testDeliverShippedDirectly() {
        // 14시 기다릴 필요 없이 바로 실행
        shippingScheduler.deliverShipped();

        // orderService.deliverAllShippedOrders() 실행됐는지 로그/검증
        // 예: DB 상태 확인
        // Assertions.assertEquals(...);
    }
}