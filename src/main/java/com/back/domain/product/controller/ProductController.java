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

@RequiredArgsConstructor   // ✅ final 필드를 생성자 주입
@Controller   // HTML 렌더링용 컨트롤러
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    // 1) 상품 목록 페이지 (HTML 반환)
    @GetMapping("/products")
    public String showProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("orderPageUrl", "/orders"); // 주문 페이지 이동 버튼 경로
        return "products";  // → templates/products.html
    }

    // 2) 상품 상세 조회 API (JSON 반환 → 모달에서 사용)
    @GetMapping("/api/products/{id}")
    @ResponseBody
    public ProductDetailDto getProductDetail(@PathVariable Integer id) {
        return productService.getProductDetail(id);
    }
}

