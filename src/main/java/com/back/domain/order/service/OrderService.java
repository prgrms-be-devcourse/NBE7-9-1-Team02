package com.back.domain.order.service;


import com.back.domain.order.dto.OrderDetailDto;
import com.back.domain.order.entity.OrderStatus;
import com.back.domain.order.entity.Order;
import com.back.domain.order.repository.OrderRepository;
import com.back.domain.mapper.Mapper;
import com.back.domain.mapper.OrderMapper;
import com.back.domain.order.dto.OrderForm;
import com.back.domain.order.dto.OrderItemDto;
import com.back.domain.order.entity.OrderProduct;

import com.back.domain.product.entity.Product;
import com.back.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    private static final int DELIVERED_TIME = 14;

    // --- 결제 ---
    @Transactional
    public Order payment(OrderForm orderForm) {
        Order newOrder = OrderMapper.toOrderEntityWithoutPrice(orderForm);
        Long totalPrice = 0L;

        for (OrderItemDto item : orderForm.getOrderItems()) {
            int updatedRows = productRepository.decreaseStock(item.getProductId(), item.getQuantity());

            //  만약 업데이트된 행이 0이라면, 재고가 부족했다는 의미
            if (updatedRows == 0) {
                throw new IllegalStateException("상품 ID " + item.getProductId() + "의 재고가 부족합니다.");
            }

            //  재고 차감에 성공 -> 총액 계산을 위해 상품 정보를 조회합니다.
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품 없음 id=" + item.getProductId()));

            totalPrice += product.getPrice() * item.getQuantity();


            OrderProduct orderProduct = OrderProduct.createOrderProduct(product, item.getQuantity());
            newOrder.addOrderProduct(orderProduct);
        }

        if (!totalPrice.equals(orderForm.getTotalPrice())) {
            throw new IllegalStateException("총 금액 불일치");
        }
        newOrder.setTotalPrice(totalPrice);
        return orderRepository.save(newOrder);
    }

    // --- 주문 조회 ---
    @Transactional(readOnly = true)
    public Order findOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 id=" + orderId));
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public OrderDetailDto getOrderDetail(Integer orderId) {
        Order order = orderRepository.findWithProductsById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음: " + orderId));

        boolean canCancel = canCancelShipment(order);
        return Mapper.toOrderDetailDto(order, canCancel);
    }

    // --- 배송 ---
    @Transactional
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
    @Transactional
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

    @Transactional
    public void deliverAllShippedOrders() {
        List<Order> list = orderRepository.findByStatus(OrderStatus.SHIPPED);
        for (Order o : list) {
            o.setStatus(OrderStatus.DELIVERED);
            log.info("배송완료 처리됨: orderId=" + o.getId());
        }
        orderRepository.saveAll(list);
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