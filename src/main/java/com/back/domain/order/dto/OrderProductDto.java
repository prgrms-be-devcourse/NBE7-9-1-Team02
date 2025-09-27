package com.back.domain.order.dto;

public record OrderProductDto(
        Integer productId,
        String productName,
        Integer quantity,
        Long price
) {}
