package com.back.service;

import com.back.dto.ProductDetailDto;

public interface ProductService {
    ProductDetailDto getProductDetail(Integer id);
}

// Repository → Service → Controller 흐름 생성