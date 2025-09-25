package com.back.admin.service;

import com.back.admin.domain.model.Order;
import com.back.admin.domain.model.OrderStatus;
import com.back.admin.dto.OrderDetailDto;
import com.back.admin.dto.OrderProductDto;
import com.back.admin.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ZoneId ZONE = ZoneId.of("Asia/Seoul");

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 상태별 주문 조회
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    // 상세 DTO 변환 (OrderDetailDto 생성부는 canCancel 포함)
    public OrderDetailDto getOrderDetail(Integer orderId) {
        Order order = orderRepository.findWithProductsById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음: " + orderId));

        List<OrderProductDto> products = order.getOrderProducts().stream()
                .map(op -> new OrderProductDto(op.getProduct().getId(), op.getProduct().getName(),
                        op.getOrderQuantity(), op.getProduct().getPrice().longValue()))
                .collect(Collectors.toList());

        boolean canCancel = canCancelShipment(order);
        return new OrderDetailDto(order.getId(), order.getEmail(), order.getOrderDate(),
                order.getStatus().name(), order.getTotalPrice(), products, canCancel);
    }

    // 배송하기 (관리자 클릭) — shippedAt 기록
    public Order shipOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new IllegalArgumentException("주문 없음: " + orderId));
        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("배송 상태로 변경할 수 없는 주문입니다.");
        }
        order.setStatus(OrderStatus.SHIPPED);
        order.setShippedAt(ZonedDateTime.now(ZONE)); // 배송 시작 시각 기록
        return order;
    }

    // 취소(배송중 -> 결제완료) - 취소 가능 시간 체크
    public Order cancelShipment(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new IllegalArgumentException("주문 없음: " + orderId));
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new IllegalStateException("취소할 수 없습니다.");
        }
        if (!canCancelShipment(order)) {
            throw new IllegalStateException("이미 취소 불가능한 시간입니다.");
        }
        order.setStatus(OrderStatus.PAID);
        order.setShippedAt(null); // 배송 취소 되면 shippedAt 초기화
        return order;
    }

    // 14:00 스케줄에서 호출 -> SHIPPED => DELIVERED
    public void deliverAllShippedOrders() {
        List<Order> list = orderRepository.findByStatus(OrderStatus.SHIPPED);
        for (Order o : list) {
            o.setStatus(OrderStatus.DELIVERED);
            // (선택) o.setDeliveredAt(ZonedDateTime.now(ZONE));
        }
        orderRepository.saveAll(list);
    }

    // 취소 허용 여부 판단
    // 정책: 관리자(또는 누가) '배송하기' 클릭해 shippedAt 기록 -> 그 시점 기준으로
    // 다음으로 올 '14:00' (같은 날 14:00 이거나 다음날 14:00) 이전이면 취소 허용.
    private boolean canCancelShipment(Order order) {
        if (order.getStatus() != OrderStatus.SHIPPED) return false;
        ZonedDateTime now = ZonedDateTime.now(ZONE);
        ZonedDateTime shippedAt = order.getShippedAt();
        if (shippedAt == null) return false;

        ZonedDateTime shippedInZone = shippedAt.withZoneSameInstant(ZONE);
        LocalTime cutoffClock = LocalTime.of(14, 0);

        // scheduledDelivery: shipped 당일 14:00 (shipped 시간이 14:00 이전 포함) 아니면 다음날 14:00
        ZonedDateTime scheduledDelivery;
        LocalTime shippedTime = shippedInZone.toLocalTime();

        if (!shippedTime.isAfter(cutoffClock)) { // shipped time <= 14:00 -> 배송은 같은날 14:00에 이뤄짐
            scheduledDelivery = shippedInZone.withHour(14).withMinute(0).withSecond(0).withNano(0);
        } else { // shipped after 14:00 -> 다음날 14:00에 배송 처리
            scheduledDelivery = shippedInZone.plusDays(1).withHour(14).withMinute(0).withSecond(0).withNano(0);
        }

        // 취소 허용: 현재 시간이 scheduledDelivery 이전인 경우
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