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


}
