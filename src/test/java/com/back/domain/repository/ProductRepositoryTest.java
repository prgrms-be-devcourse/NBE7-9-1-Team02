package com.back.domain.repository;

import com.back.domain.product.entity.Product;
import com.back.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품저장_불러오기() {
        // given
        Product product = new Product();
        product.setName("테스트 커피");
        product.setPrice(5000L);
        product.setStock(100);

        // when
        Product saved = productRepository.save(product);

        // then
        Product found = productRepository.findById(saved.getProductId()).orElseThrow();
        assertThat(found.getName()).isEqualTo("테스트 커피");
    }
}