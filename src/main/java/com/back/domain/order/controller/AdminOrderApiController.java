package com.back.domain.order.controller;

import com.back.domain.order.dto.OrderDetailDto;
import com.back.domain.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderApiController {

    private final OrderService orderService;

    public AdminOrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<OrderDetailDto> detail(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrderDetail(id));
    }

    @PostMapping("/{id}/ship")
    public ResponseEntity<?> ship(@PathVariable Integer id) {
        try {
            orderService.shipOrder(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Integer id) {
        try {
            orderService.cancelShipment(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}