package com.back.admin.domain.model;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PAID("결제완료"),
    DELIVERING("배송중"),
    DELIVERED("배송완료");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
