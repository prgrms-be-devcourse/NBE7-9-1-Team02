package com.back.admin.controller;

import com.back.admin.domain.model.Order;
import com.back.admin.domain.model.OrderStatus;
import com.back.admin.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
public class OrderListController {

    private final OrderService orderService;

    // 주문 목록 홈 화면
    @GetMapping
    public String adminOrderListStaus(@RequestParam(value = "status", required = false) OrderStatus status, Model model) {

        List<Order> orders = (status == null)
                ? orderService.findAllOrders() // 전체 주문 리스트
                : orderService.findOrdersByStatus(status); // 상태별 필터링된 주문 리스트

        model.addAttribute("orders", orders);  // 전체 주문
        model.addAttribute("currentStatus", status);  // 상태별 필터링

        return "admin/orderList";  // 뷰는 하나만 사용, 버튼 클릭으로 상태 필터링
    }
}
