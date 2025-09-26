package com.back.jongwoo.service;

import com.back.jongwoo.Product;
import com.back.jongwoo.ProductDetail.ProductDetail;
import com.back.jongwoo.dto.ProductDetailDto;
import com.back.jongwoo.repository.ProductRepository;
import com.back.jongwoo.repository.ProductDetailRepository;
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
