package com.back.service;

import com.back.domain.Product;
import com.back.domain.ProductDetail;
import com.back.dto.ProductDetailDto;
import com.back.repository.ProductRepository;
import com.back.repository.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

    @Override
    public ProductDetailDto getProductDetail(Integer id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음: " + id));

        ProductDetail d = productDetailRepository.findByProduct_ProductId(id)
                .orElse(null);

        return ProductDetailDto.builder()
                .productId(p.getProductId())
                .origin(d != null ? d.getOrigin() : "상세 준비중")
                .flavorAroma(d != null ? d.getFlavorAroma() : "상세 준비중")
                .feature(d != null ? d.getFeature() : "상세 준비중")
                .price(p.getPrice())   // ✅ 가격 채움
                .build();
    }
}
