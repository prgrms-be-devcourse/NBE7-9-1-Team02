package com.back.domain.product.service;

import com.back.domain.product.dto.ProductDetailDto;
import com.back.domain.product.entity.Product;
import com.back.domain.product.entity.ProductDetail;
import com.back.domain.product.repository.ProductRepository;
import com.back.domain.product.repository.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;   // ✅ 상세 정보도 사용

    // 전체 조회
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // 저장
    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }

    // 카운트
    public long count() {
        return productRepository.count();
    }

    // ✅ 상품 상세 조회
    public ProductDetailDto getProductDetail(Integer productId) {
        // 기본 상품 정보
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음 id=" + productId));

        // 상세 정보 (없을 수도 있으니 Optional 처리)
        ProductDetail d = productDetailRepository.findByProduct_ProductId(productId).orElse(null);

        return ProductDetailDto.builder()
                .productId(p.getProductId())
                .origin(d != null ? d.getOrigin() : "상세 준비중")
                .flavorAroma(d != null ? d.getFlavorAroma() : "상세 준비중")
                .feature(d != null ? d.getFeature() : "상세 준비중")
                .price(p.getPrice())
                .build();
    }
}

