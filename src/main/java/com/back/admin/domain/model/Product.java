package com.back.admin.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {  // 재고 상품 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer id;        // 상품 코드

    private String name;       // 상품명
    private Integer price;     // 상품 가격
    private Integer quantity;  // 재고량

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private ProductDetail productDetail; // 상품 상세 설명

    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orderProducts; // 주문한 상품
}