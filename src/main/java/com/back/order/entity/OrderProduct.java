package com.back.order.entity;

import com.back.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 하나의 주문은 여러 주문상품을 가짐
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY) // 하나의 상품은 여러 주문상품에 포함됨
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity; // 주문 수량
    private int price;    // 주문 당시 가격
}