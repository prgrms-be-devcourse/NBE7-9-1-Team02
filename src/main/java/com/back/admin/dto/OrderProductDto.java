package com.back.admin.dto;

public record OrderProductDto(
        Integer productId,
        String productName,
        Integer quantity,
        Long price
) {}
