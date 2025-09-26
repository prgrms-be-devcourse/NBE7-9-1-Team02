package com.back.order.service;

import com.back.order.dto.OrderForm;
import com.back.order.dto.OrderItemDto;
import com.back.order.entity.Orders;
import com.back.order.repository.OrderRepository;
import com.back.product.entity.Product;
import com.back.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Orders payment(OrderForm orderForm) {
        // === 서버 사이드에서 총 금액 다시 계산 ===
        int serverCalculatedTotalPrice = 0;
        for (OrderItemDto itemDto : orderForm.getOrderItems()) {
            // DB에서 상품 정보를 다시 조회 (가격을 신뢰하기 위함)
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. id=" + itemDto.getProductId()));

            // 조회한 상품의 가격을 기준으로 금액 계산
            serverCalculatedTotalPrice += product.getPrice() * itemDto.getQuantity();

        }

        // 클라이언트가 계산한 금액과 서버가 계산한 금액이 다른지 검증
        if (serverCalculatedTotalPrice != orderForm.getTotalPrice()) {
            // 금액이 다르면 해킹 시도일 수 있으므로 예외 처리
            throw new IllegalStateException("주문 금액이 일치하지 않습니다.");
        }

        // Orders 엔티티 생성
        Orders newOrder = new Orders(orderForm.getEmail(), orderForm.getCustomerName(), orderForm.getAddress(),orderForm.getZipcode(),serverCalculatedTotalPrice);

        return orderRepository.save(newOrder);
    }

    // 주문 내역 조회를 위한 메서드 추가
    public Orders findOrderById(int orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다. id=" + orderId));
    }
}
