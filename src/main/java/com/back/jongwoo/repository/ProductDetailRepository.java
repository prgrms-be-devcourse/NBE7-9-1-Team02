package com.back.jongwoo.repository;

import com.back.jongwoo.ProductDetail.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    Optional<ProductDetail> findByProduct_ProductId(Integer productId);
}
