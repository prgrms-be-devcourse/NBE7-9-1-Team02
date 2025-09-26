package com.back.domain.product.service;

import com.back.jongwoo.ProductDetail.ProductDetail;
import com.back.jongwoo.dto.ProductDetailDto;
import com.back.jongwoo.repository.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

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

    // 상세 조회
    public ProductDetailDto getProductDetail(Integer productId) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음 id=" + productId));
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