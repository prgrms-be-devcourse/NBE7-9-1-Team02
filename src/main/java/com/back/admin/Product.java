package com.back.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {  // 재고 상품 정보
    private Integer id;        // 상품 코드
    private String name;       // 상품명
    private Integer price;     // 상품 가격
    private Integer quantity;  // 재고량

    public Product(Integer id, String name, Integer price, Integer quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
