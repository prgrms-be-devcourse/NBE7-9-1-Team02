package com.back.admin.service;

import com.back.admin.domain.model.Order;
import com.back.admin.domain.model.OrderStatus;
import com.back.admin.dto.OrderDetailDto;
import com.back.admin.dto.OrderProductDto;
import com.back.admin.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private static final ZoneOffset KST_OFFSET = ZoneOffset.ofHours(9); // Asia/Seoul 기준

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public OrderDetailDto getOrderDetail(Integer orderId) {
        Order order = orderRepository.findWithProductsById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음: " + orderId));

        List<OrderProductDto> products = order.getOrderProducts().stream()
                .map(op -> new OrderProductDto(op.getProduct().getId(), op.getProduct().getName(),
                        op.getOrderQuantity(), op.getProduct().getPrice().longValue()))
                .collect(Collectors.toList());

        boolean canCancel = canCancelShipment(order);
        return new OrderDetailDto(order.getId(), order.getCustomerName(), order.getEmail(), order.getAddress(), order.getZipcode(), order.getOrderDate(),
                order.getStatus().name(), order.getTotalPrice(), products, canCancel);
    }

    // 배송하기 — shippedAt 기록
    public Order shipOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음: " + orderId));

        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("배송 상태로 변경할 수 없는 주문입니다.");
        }

        order.setStatus(OrderStatus.SHIPPED);
        order.setShippedAt(OffsetDateTime.now(KST_OFFSET)); // OffsetDateTime으로 변경
        return order;
    }

    // 배송 취소
    public Order cancelShipment(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음: " + orderId));

        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new IllegalStateException("취소할 수 없습니다.");
        }
        if (!canCancelShipment(order)) {
            throw new IllegalStateException("이미 취소 불가능한 시간입니다.");
        }

        order.setStatus(OrderStatus.PAID);
        order.setShippedAt(null); // 배송 취소 시 초기화
        return order;
    }

    public void deliverAllShippedOrders() {
        List<Order> list = orderRepository.findByStatus(OrderStatus.SHIPPED);
        for (Order o : list) {
            o.setStatus(OrderStatus.DELIVERED);
        }
        orderRepository.saveAll(list);
    }

    // 취소 가능 여부 판단
    private boolean canCancelShipment(Order order) {
        if (order.getStatus() != OrderStatus.SHIPPED) return false;
        OffsetDateTime now = OffsetDateTime.now(KST_OFFSET);
        OffsetDateTime shippedAt = order.getShippedAt();
        if (shippedAt == null) return false;

        // shippedAt을 KST 기준으로 변환
        OffsetDateTime shippedInKST = shippedAt.withOffsetSameInstant(KST_OFFSET);
        LocalTime cutoffClock = LocalTime.of(14, 0);

        OffsetDateTime scheduledDelivery;
        LocalTime shippedTime = shippedInKST.toLocalTime();

        if (!shippedTime.isAfter(cutoffClock)) { // 14:00 이전이면 당일 14:00
            scheduledDelivery = shippedInKST.withHour(14).withMinute(0).withSecond(0).withNano(0);
        } else { // 이후면 다음날 14:00
            scheduledDelivery = shippedInKST.plusDays(1).withHour(14).withMinute(0).withSecond(0).withNano(0);
        }

        return now.isBefore(scheduledDelivery);
    }
}

/**
 * 설명(정책 정리)
 *
 * 관리자가 배송하기 클릭 → shippedAt = 현재 시각 기록.
 *
 * canCancelShipment는 shippedAt 기준으로 해당 배송에 적용될 14:00(같은날 또는 다음날) 을 계산한다. 그 14:00 이전이면 취소 허용. (즉, 배송 처리가 실제로 시행되기 전까지만 취소 가능)
 *
 * deliverAllShippedOrders()는 매일 14:00에 호출되어 SHIPPED → DELIVERED 변환.
 *
 * 위 규칙은 "배송이 매일 14:00에 일괄 처리된다"는 전제하에 자연스럽게 작동함. (테스트 시 cron을 매분으로 바꿔서 동작 확인 가능)
 */