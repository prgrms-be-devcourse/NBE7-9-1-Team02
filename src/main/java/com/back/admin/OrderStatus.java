package com.back.admin;

public enum OrderStatus {
    PAYMENT_COMPLETED("결제완료"),
    DELIVERING("배송중"),
    DELIVERED("배송완료");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    // getter
    public String getDescription() {
        return description;
    }
}
