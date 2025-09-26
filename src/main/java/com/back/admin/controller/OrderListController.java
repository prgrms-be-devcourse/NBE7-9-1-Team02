package com.back.admin.controller;

import com.back.admin.domain.model.Order;
import com.back.admin.domain.model.OrderStatus;
import com.back.admin.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
public class OrderListController {

    private final OrderService orderService;

    // 주문 목록 홈 화면
    @GetMapping
    public String orderList(@RequestParam(required = false) String status, Model model){

        List<Order> orders;

        if (status != null) {
            // 문자열로 넘어온 상태를 Enum으로 변환
            OrderStatus orderStatus = OrderStatus.valueOf(status);
            orders = orderService.getOrdersByStatus(orderStatus); // 상태별 필터링된 주문 리스트
            model.addAttribute("currentStatus", orderStatus);
        } else {
            orders = orderService.getAllOrders(); // 전체 주문 리스트
            model.addAttribute("currentStatus", null);
        }

        model.addAttribute("orders", orders);
        return "admin/orderList";  // Thymeleaf template
    }
}
