package com.back.domain.order.service;

import com.back.admin.domain.model.OrderStatus;
import com.back.order.dto.OrderForm;
import com.back.order.dto.OrderItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;

    private static final ZoneOffset KST_OFFSET = ZoneOffset.ofHours(9);

    // --- 결제 ---
    public Orders payment(OrderForm orderForm) {
        int totalPrice = 0;
        for (OrderItemDto item : orderForm.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품 없음 id=" + item.getProductId()));
            totalPrice += product.getPrice() * item.getQuantity();
        }
        if (totalPrice != orderForm.getTotalPrice()) {
            throw new IllegalStateException("총 금액 불일치");
        }
        Orders newOrder = new Orders(orderForm.getEmail(), orderForm.getCustomerName(),
                orderForm.getAddress(), orderForm.getZipcode(), (long) totalPrice);
        return ordersRepository.save(newOrder);
    }

    // --- 주문 조회 ---
    public Orders findOrderById(Integer orderId) {
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 id=" + orderId));
    }

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    public List<Orders> getOrdersByStatus(OrderStatus status) {
        return ordersRepository.findByStatus(status);
    }

    // --- 배송 ---
    public Orders shipOrder(Integer orderId) {
        Orders order = findOrderById(orderId);
        if (order.getStatus() != OrderStatus.PAYMENT_COMPLETED) {
            throw new IllegalStateException("배송 불가 상태");
        }
        order.setStatus(OrderStatus.SHIPPED);
        order.setShippedAt(OffsetDateTime.now(KST_OFFSET));
        return order;
    }

    public Orders cancelShipment(Integer orderId) {
        Orders order = findOrderById(orderId);
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new IllegalStateException("취소 불가");
        }
        if (!canCancelShipment(order)) {
            throw new IllegalStateException("이미 취소 불가 시간");
        }
        order.setStatus(OrderStatus.PAYMENT_COMPLETED);
        order.setShippedAt(null);
        return order;
    }

    public void deliverAllShippedOrders() {
        List<Orders> list = ordersRepository.findByStatus(OrderStatus.SHIPPED);
        for (Orders o : list) {
            o.setStatus(OrderStatus.DELIVERED);
            log.info("배송완료 처리됨: orderId=" + o.getId());
        }
        ordersRepository.saveAll(list);
    }

    private boolean canCancelShipment(Orders order) {
        if (order.getStatus() != OrderStatus.SHIPPED) return false;
        OffsetDateTime now = OffsetDateTime.now(KST_OFFSET);
        OffsetDateTime shipped = order.getShippedAt();
        if (shipped == null) return false;

        OffsetDateTime scheduledDelivery = shipped.withOffsetSameInstant(KST_OFFSET)
                .withHour(14).withMinute(0).withSecond(0).withNano(0);
        if (shipped.toLocalTime().isAfter(LocalTime.of(14,0))) {
            scheduledDelivery = scheduledDelivery.plusDays(1);
        }
        return now.isBefore(scheduledDelivery);
    }
}
