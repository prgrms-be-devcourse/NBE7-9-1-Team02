package com.back.domain.mapper;

import com.back.domain.order.dto.OrderForm;
import com.back.domain.order.entity.Order;

public class OrderMapper {

    // private 생성자로 외부에서 객체 생성 방지
    private OrderMapper() {}

    public static Order toOrderEntityWithoutPrice(OrderForm orderForm) {
        return new Order(
                orderForm.getEmail(),
                orderForm.getCustomerName(),
                orderForm.getAddress(),
                orderForm.getZipcode()
        );
    }
}
