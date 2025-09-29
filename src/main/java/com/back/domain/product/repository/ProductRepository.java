package com.back.domain.product.repository;

import com.back.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Modifying // 이 쿼리가 INSERT, UPDATE, DELETE 등 데이터를 변경하는 쿼리임을 알립니다.
    @Query("UPDATE Product p SET p.stock = p.stock - :quantity " +
            "WHERE p.productId = :id AND p.stock >= :quantity")
    int decreaseStock(@Param("id") Integer id, @Param("quantity") int quantity);

    @Query("SELECT p FROM Product p JOIN FETCH p.productDetail")
    List<Product> findAllWithDetail();
}
