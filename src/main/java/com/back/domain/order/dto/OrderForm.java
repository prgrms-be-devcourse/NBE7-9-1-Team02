package com.back.domain.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderForm {

    @Email
    @NotEmpty(message = "이메일은 필수 항목입니다.")
    private String email;

    @NotBlank(message = "주문자명은 필수 항목입니다.")
    private String customerName;

    @NotBlank(message = "주소지는 필수 항목입니다.")
    private String address;
    private int zipcode;
    private List<OrderItemDto> orderItems; // 여러 상품 정보를 리스트로 받음
    private int totalPrice;
}
