package com.back.domain.order.service;

import com.back.domain.order.dto.OrderDetailDto;
import com.back.domain.order.dto.OrderProductDto;
import com.back.domain.order.entity.OrderStatus;
import com.back.domain.order.entity.Order;
import com.back.domain.order.repository.OrdersRepository;
import com.back.domain.product.entity.Product;
import com.back.domain.product.repository.ProductRepository;
import com.back.domain.order.dto.OrderForm;
import com.back.domain.order.dto.OrderItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;

    private static final int DELIVERED_TIME = 14;

    private static final ZoneOffset KST_OFFSET = ZoneOffset.ofHours(9);

    // --- 결제 ---
    public Order payment(OrderForm orderForm) {
        int totalPrice = 0;
        for (OrderItemDto item : orderForm.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품 없음 id=" + item.getProductId()));
            if (product.getStock() < item.getQuantity()) {
                throw new IllegalStateException(product.getName() + " 상품의 재고가 부족합니다.");
            }

            //  재고 차감
            product.setStock(product.getStock() - item.getQuantity());

            totalPrice += product.getPrice() * item.getQuantity();
        }
        if (totalPrice != orderForm.getTotalPrice()) {
            throw new IllegalStateException("총 금액 불일치");
        }
        Order newOrder = new Order(orderForm.getEmail(), orderForm.getCustomerName(),
                orderForm.getAddress(), orderForm.getZipcode(), (long) totalPrice);
        return ordersRepository.save(newOrder);
    }

    // --- 주문 조회 ---
    public Order findOrderById(Integer orderId) {
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 id=" + orderId));
    }

    public List<Order> getAllOrders() {
        return ordersRepository.findAll();
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return ordersRepository.findByStatus(status);
    }

    public OrderDetailDto getOrderDetail(Integer orderId) {
        Order order = ordersRepository.findWithProductsById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음: " + orderId));

        List<OrderProductDto> products = order.getOrderProducts().stream()
                .map(op -> new OrderProductDto(op.getProduct().getProductId(), op.getProduct().getName(),
                        op.getQuantity(), op.getProduct().getPrice()))
                .collect(Collectors.toList());

        boolean canCancel = canCancelShipment(order);
        return new OrderDetailDto(order.getId(), order.getCustomerName(), order.getEmail(), order.getAddress(), order.getZipcode(), order.getOrderDate(),
                order.getStatus().name(), order.getTotalPrice(), products, canCancel);
    }

    // --- 배송 ---
    public Order shipOrder(Integer orderId) {
        Order order = findOrderById(orderId);
        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("배송 불가 상태");
        }
        order.setStatus(OrderStatus.SHIPPED);
        order.setShippedAt(LocalDateTime.now(ZoneOffset.ofHours(9))); // KST 기준
        return order;
    }

    // --- 배송 취소 ---
    public Order cancelShipment(Integer orderId) {
        Order order = findOrderById(orderId);
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new IllegalStateException("취소 불가");
        }
        if (!canCancelShipment(order)) {
            throw new IllegalStateException("취소 불가 시간");
        }
        order.setStatus(OrderStatus.PAID);
        order.setShippedAt(null);
        return order;
    }

    // 취소 가능 여부 판단
    private boolean canCancelShipment(Order order) {
        if (order.getStatus() != OrderStatus.SHIPPED) return false;
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(9));
        LocalDateTime shipped = order.getShippedAt();
        if (shipped == null) return false;

        LocalDateTime scheduledDelivery = shipped.withHour(DELIVERED_TIME);
        if (shipped.toLocalTime().isAfter(LocalTime.of(DELIVERED_TIME,0))) {
            scheduledDelivery = scheduledDelivery.plusDays(1);
        }
        return now.isBefore(scheduledDelivery);
    }

    public void deliverAllShippedOrders() {
        List<Order> list = ordersRepository.findByStatus(OrderStatus.SHIPPED);
        for (Order o : list) {
            o.setStatus(OrderStatus.DELIVERED);
            log.info("배송완료 처리됨: orderId=" + o.getId());
        }
        ordersRepository.saveAll(list);
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