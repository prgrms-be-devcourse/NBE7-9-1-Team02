package com.back.admin.service;

import com.back.admin.domain.model.Order;
import com.back.admin.domain.model.OrderStatus;
import com.back.admin.dto.OrderDetailDto;
import com.back.admin.dto.OrderProductDto;
import com.back.admin.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    // 상세 DTO 변환
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

    // 배송하기 (관리자 클릭)
    public Order shipOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("배송 상태로 변경할 수 없는 주문입니다.");
        }
        order.setStatus(OrderStatus.SHIPPED);
        // 저장은 트랜잭션 커밋 시 수행(Repository는 이미 관리중)
        return order;
    }

    // 취소(배송중 -> 결제완료) - 취소 가능 시간 체크
    public Order cancelShipment(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new IllegalStateException("취소할 수 있는 상태가 아닙니다.");
        }
        if (!canCancelShipment(order)) {
            throw new IllegalStateException("이미 취소 불가능한 시간입니다.");
        }
        order.setStatus(OrderStatus.PAID);
        return order;
    }

    // 14:00 스케줄에서 호출 -> DELIVERING => DELIVERED
    public void deliverAllShippedOrders() {
        List<Order> list = orderRepository.findByStatus(OrderStatus.SHIPPED);
        for (Order o : list) {
            o.setStatus(OrderStatus.DELIVERED);
        }
        orderRepository.saveAll(list);
    }

    // 주문이 취소 가능한지 (현재 시간 < 오늘 14:00) — 주문 객체 상태도 체크
    private boolean canCancelShipment(Order order) {
        if (order.getStatus() != OrderStatus.SHIPPED) return false;
        ZonedDateTime now = ZonedDateTime.now(ZONE);
        LocalDate today = now.toLocalDate();
        ZonedDateTime cutoff = today.atTime(14, 0).atZone(ZONE);
        return now.isBefore(cutoff);
    }
}
