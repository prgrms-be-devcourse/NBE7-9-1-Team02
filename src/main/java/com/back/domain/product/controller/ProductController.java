package com.back.domain.product.controller;

import com.back.domain.product.dto.ProductDetailDto;
import com.back.domain.product.repository.ProductRepository;
import com.back.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    // 1) 상품 목록 페이지
    @GetMapping("/products")
    public String showProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("orderPageUrl", "/orders");
        return "product/products";  // → templates/products.html
    }

    // 2) 팝업 API - JSON 반환 → 팝업
    @GetMapping("/api/products/{id}")
    @ResponseBody
    public ProductDetailDto getProductDetail(@PathVariable Integer id) {
        return productService.getProductDetail(id);
    }
}

