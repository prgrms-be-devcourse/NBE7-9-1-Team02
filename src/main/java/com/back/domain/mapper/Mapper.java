package com.back.domain.mapper;

import com.back.domain.order.dto.OrderDetailDto;
import com.back.domain.order.dto.OrderProductDto;
import com.back.domain.order.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    public static OrderDetailDto toOrderDetailDto(Order order, boolean canCancel) {
        List<OrderProductDto> products = order.getOrderProducts().stream()
                .map(op -> new OrderProductDto(
                        op.getProduct().getProductId(),
                        op.getProduct().getName(),
                        op.getQuantity(),
                        op.getProduct().getPrice()
                ))
                .collect(Collectors.toList());

        return new OrderDetailDto(
                order.getId(),
                order.getCustomerName(),
                order.getEmail(),
                order.getAddress(),
                order.getZipcode(),
                order.getOrderDate(),
                order.getStatus().name(),
                order.getTotalPrice(),
                products,
                canCancel
        );
    }
}
