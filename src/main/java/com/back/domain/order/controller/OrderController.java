package com.back.domain.order.controller;

import com.back.domain.order.dto.OrderForm;
import com.back.domain.order.entity.Orders;
import com.back.domain.order.service.OrderService;
import com.back.domain.product.entity.Product;
import com.back.domain.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final ObjectMapper objectMapper; // Jackson ObjectMapper 주입

    @GetMapping //주문페이지 접속
    public String orderPage(Model model) {

        List<Product> productList = productService.findAll();

        model.addAttribute("orderForm", new OrderForm());
        model.addAttribute("products", productList);
        // GET 요청 시에는 장바구니가 비어있으므로 빈 JSON 배열을 전달
        model.addAttribute("initialCartItemsJson", "[]");

        return "order/orderPage";
    }


    @PostMapping("/payment")    // 주문 결제
    public String payment(@Valid OrderForm orderForm, BindingResult bindingResult,
            Model model) {
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                log.info("Validation Error: {}", error.getDefaultMessage());
            });
        List<Product> productList = productService.findAll();
        model.addAttribute("products", productList);
            try {
                // 유효성 검사에 실패했을 때, 사용자가 보낸 orderItems를 JSON 문자열로 변환
                String cartJson = objectMapper.writeValueAsString(orderForm.getOrderItems());
                model.addAttribute("initialCartItemsJson", cartJson);
            } catch (JsonProcessingException e) {
                // JSON 변환 실패 시 비상 처리 (보통은 발생하지 않음)
                model.addAttribute("initialCartItemsJson", "[]");
            }
        return "order/orderPage"; // 오류가 생기면 데이터를 가지고 주문페이지
        }

        Orders createOrder = orderService.payment(orderForm);
        return "redirect:/orders/"+ createOrder.getId() + "/completed"; //올바르게 주문이 완료되면 결제완료 페이지 이동
    }

    @GetMapping("/{orderId}/completed")
    public String completed(@PathVariable("orderId") Integer orderId, Model model) {

        Orders order = orderService.findOrderById(orderId);
        model.addAttribute("order", order);
        return "order/completed"; // completed.html 템플릿을 렌더링
    }
}
