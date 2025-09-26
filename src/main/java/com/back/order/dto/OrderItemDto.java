package com.back.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private int productId;
    private int quantity;
}
