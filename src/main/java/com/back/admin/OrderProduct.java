package com.back.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProduct {  // 주문 상품 정보
    private Integer id;        // 상품 코드
    private String name;       // 상품명
    private Integer price;     // 상품별 금액
    private Integer quantity;  // 상품별 주문 개수


    public OrderProduct(Integer id, String name, Integer price, Integer quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return quantity * price;
    }
}
