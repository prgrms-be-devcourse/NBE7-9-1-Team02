package com.back.domain.product.controller;

import com.back.domain.product.repository.ProductRepository;
import com.back.domain.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller   // HTML 렌더링을 위해 Controller 유지
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;   // 서비스 주입 (상세조회용)

    // 생성자 주입
    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    //  1) 상품 목록 페이지 (HTML)
    @GetMapping("/products")
    public String showProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("orderPageUrl", "/order"); // 주문 페이지 이동 버튼 경로
        return "products";  // → templates/products.html 로 이동
    }

    //  2) 상품 상세 조회 API (JSON 반환 → 모달에서 사용)
//    @GetMapping("/products/{id}")
//    @ResponseBody
//    public ProductDetailDto getProductDetail(@PathVariable Integer id) {
//        return productService.getProductDetail(id);
//    }
}
