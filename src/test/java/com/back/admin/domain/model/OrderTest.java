package com.back.admin.domain.model;

import com.back.admin.repository.OrderRepository;
import com.back.admin.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;

    @Test
    void insertSampleOrder() {
        Product product = new Product();
        product.setName("라떼");
        product.setPrice(5000);
        product.setQuantity(50);
        productRepository.save(product);

        Order order = new Order();
        order.setEmail("user@test.com");
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        OrderProduct op = new OrderProduct();
        op.setProduct(product);
        op.setOrder(order);
        op.setOrderQuantity(1);

        order.setOrderProducts(List.of(op));
        orderRepository.save(order);
    }
}
