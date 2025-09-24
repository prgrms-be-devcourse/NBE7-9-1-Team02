package com.back.admin.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailDto(
        Integer orderId,
        String email,
        LocalDateTime orderDate,
        String status,        // 문자열: PAID/SHIPPED/DELIVERED
        Long totalPrice,
        List<OrderProductDto> products,
        boolean canCancel     // front에서 버튼 enabled/visible 제어용(서버비즈니스 규칙)
) {}
