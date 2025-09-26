package com.back.domain.order.entity;

import com.back.domain.product.entity.Product;
import com.back.order.entity.Orders;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_product")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Integer id;  // 통합 후 id 타입 Integer로 통일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders order; // Orders로 통일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "order_quantity")
    private Integer quantity; // 수량 필드 이름 통일

    @Column(name = "price")
    private Integer price;    // 주문 당시 가격
}