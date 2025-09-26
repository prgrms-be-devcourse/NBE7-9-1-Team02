package com.back.domain.product.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {

    Optional<ProductDetail> findByProduct_ProductId(Integer productId);
}
