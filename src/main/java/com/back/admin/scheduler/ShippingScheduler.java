package com.back.admin.scheduler;

import com.back.admin.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ShippingScheduler {
    private final OrderService orderService;

    public ShippingScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    // 매일 14:00 (Asia/Seoul) 에 실행
    @Scheduled(cron = "0 0 14 * * *", zone = "Asia/Seoul")
    public void doDeliveriesAtTwoPm() {
        orderService.deliverAllShippedOrders();
        // 필요 시 로그/모니터링 추가
    }
}
