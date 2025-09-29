package com.back.domain.domain.model;

import com.back.domain.order.entity.OrderProduct;
import com.back.domain.order.entity.Order;
import com.back.domain.order.repository.OrderRepository;

import com.back.domain.product.repository.ProductRepository;
import com.back.domain.product.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderProductTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testOrderProductSummary() {
        // 1. 상품 생성
        Product shampoo = new Product();
        shampoo.setName("샴푸");
        shampoo.setPrice(5000L);
        shampoo.setStock(100);
        productRepository.save(shampoo);

        Product rinse = new Product();
        rinse.setName("린스");
        rinse.setPrice(3000L);
        rinse.setStock(100);
        productRepository.save(rinse);

        // 2. 주문 생성
        Order order = new Order();
        order.setCustomerName("김민지");
        order.setEmail("test@naver.com");
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice((5000*2 + 3000L));

        // 주문 상품 연결
        OrderProduct op1 = new OrderProduct();
        op1.setOrder(order);
        op1.setProduct(shampoo);
        op1.setQuantity(2);
        op1.setPrice(5000L);

        OrderProduct op2 = new OrderProduct();
        op2.setOrder(order);
        op2.setProduct(rinse);
        op2.setQuantity(1);
        op2.setPrice(3000L);

        order.setOrderProducts(List.of(op1, op2));

        // 3. DB 저장
        orderRepository.save(order);

        // 4. 조회 및 상품 요약 확인
        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow();

        String summary = savedOrder.getOrderProducts().size() > 0
                ? savedOrder.getOrderProducts().get(0).getProduct().getName() + " 외 " +
                (savedOrder.getOrderProducts().size() - 1) + "개"
                : "상품 없음";

        System.out.println("상품 요약: " + summary);

        assertThat(summary).isEqualTo("샴푸 외 1개");
    }
}