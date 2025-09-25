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

    // 실제 운영: 매일 14:00 (서울)
    @Scheduled(cron = "0 0 14 * * *", zone = "Asia/Seoul")
    public void deliverShipped() {
        orderService.deliverAllShippedOrders();
    }
}
