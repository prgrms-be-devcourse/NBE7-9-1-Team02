package com.back.jongwoo.service;

import com.back.jongwoo.dto.ProductDetailDto;

public interface ProductService {
    ProductDetailDto getProductDetail(Integer id);
}

// Repository → Service → Controller 흐름 생성