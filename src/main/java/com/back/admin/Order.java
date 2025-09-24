package com.back.admin;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Order { // 주문 정보
    private Integer id;                // 주문 번호
    private String email;           // 고객 이메일
    private LocalDateTime orderDate;// 주문 일시
    private OrderStatus status;     // 주문 상태
    private List<OrderProduct> orderProducts; // 주문 상품


    public Order(Integer id, String email, LocalDateTime orderDate, OrderStatus status, List<OrderProduct> orderProducts) {
        this.id = id;
        this.email = email;
        this.orderDate = orderDate;
        this.status = status;
        this.orderProducts = orderProducts;
    }

    public  int  getTotalPrice() {
        return orderProducts.stream().mapToInt(OrderProduct::getTotalPrice).sum();
    }
}

