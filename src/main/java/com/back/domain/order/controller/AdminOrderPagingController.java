package com.back.domain.order.controller;

import com.back.domain.order.entity.OrderStatus;
import com.back.domain.order.entity.Order;
import com.back.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminOrderPagingController {

    // 현재 페이지, 페이지 크기, 상태별 필터를 모두 처리
    private final OrderRepository orderRepository;


    //@GetMapping("/admin/orders")
//    public String listOrders(
//            @PageableDefault(size = 10, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable,
//            @RequestParam(required = false) OrderStatus status,
//            Model model) {
//
//        Page<Order> ordersPage = (status == null)
//                ? orderRepository.findAll(pageable)   // 전체 조회 (페이징 O)
//                : orderRepository.findByStatus(status, pageable); // 상태별 조회 (페이징 O)
//
//        model.addAttribute("ordersPage", ordersPage);
//        model.addAttribute("currentStatus", status);
//        return "admin/orderList";
//    }
}
