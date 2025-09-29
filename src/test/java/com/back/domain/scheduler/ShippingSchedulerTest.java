package com.back.domain.scheduler;

import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderStatus;
import com.back.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ShippingSchedulerTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShippingScheduler shippingScheduler;

    @Test
    void testDeliverShippedOrders() {
        // given - 배송중인 주문 저장
        Order order = new Order();
        order.setEmail("scheduler@test.com");
        order.setStatus(OrderStatus.SHIPPED);
        order.setTotalPrice(20000L);
        order.setOrderDate(LocalDateTime.now());
        order.setShippedAt(LocalDateTime.now());
        order.setAddress("아아시 야야구");
        order.setZipcode("12345");
        order.setCustomerName("dkdd");
        orderRepository.save(order);

        // when - 스케줄러 메서드 직접 실행
        shippingScheduler.deliverShipped();

        // then - 상태가 DELIVERED로 변경됐는지 확인
        Order found = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }
}