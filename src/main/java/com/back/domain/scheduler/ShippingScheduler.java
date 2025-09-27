package com.back.domain.scheduler;

import com.back.domain.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
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
        log.info("[14:00] delivered shipping task");
    }

    // 1분마다 테스트용 실행
//    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
//    public void deliverShipped() {
//        orderService.deliverAllShippedOrders();
//        log.info("[TEST] delivered shipping task");
//    }
}
