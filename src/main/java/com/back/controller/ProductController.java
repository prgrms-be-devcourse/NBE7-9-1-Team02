package com.back.controller;

import com.back.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller   // ✅ RestController → Controller 로 변경
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ✅ 상품 목록을 HTML 페이지(products.html)로 전달
    @GetMapping("/products")
    public String showProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("orderPageUrl", "/order"); // 주문 페이지 이동 버튼 경로
        return "products";  // → templates/products.html 로 이동
    }
}

