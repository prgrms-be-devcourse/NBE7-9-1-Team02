package com.back.admin.scheduler;

import com.back.admin.domain.model.Order;
import com.back.admin.domain.model.OrderStatus;
import com.back.admin.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

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
        order.setShippedAt(OffsetDateTime.now());
        order.setAddress("아아시 야야구");
        order.setZipcode(12345);
        order.setCustomerName("dkdd");
        orderRepository.save(order);

        // when - 스케줄러 메서드 직접 실행
        shippingScheduler.deliverShipped();

        // then - 상태가 DELIVERED로 변경됐는지 확인
        Order found = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    void testDeliverMultipleShippedOrders() {
        // given
        Order o1 = new Order();
        o1.setEmail("s1@test.com");
        o1.setStatus(OrderStatus.SHIPPED);
        o1.setTotalPrice(10000L);
        o1.setOrderDate(LocalDateTime.now());
        o1.setShippedAt(OffsetDateTime.now());

        Order o2 = new Order();
        o2.setEmail("s2@test.com");
        o2.setStatus(OrderStatus.SHIPPED);
        o2.setTotalPrice(20000L);
        o2.setOrderDate(LocalDateTime.now());
        o2.setShippedAt(OffsetDateTime.now());

        orderRepository.save(o1);
        orderRepository.save(o2);

        // when
        shippingScheduler.deliverShipped();

        // then
        assertThat(orderRepository.findById(o1.getId()).get().getStatus())
                .isEqualTo(OrderStatus.DELIVERED);
        assertThat(orderRepository.findById(o2.getId()).get().getStatus())
                .isEqualTo(OrderStatus.DELIVERED);
    }

}