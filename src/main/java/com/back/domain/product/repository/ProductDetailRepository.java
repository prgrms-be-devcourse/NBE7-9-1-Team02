package com.back.domain.product.repository;

import com.back.domain.product.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {

    Optional<ProductDetail> findByProduct_ProductId(Integer productId);
}
