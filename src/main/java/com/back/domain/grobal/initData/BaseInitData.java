package com.back.domain.grobal.initData;

import com.back.domain.product.entity.Product;
import com.back.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    @Autowired
    @Lazy
    private BaseInitData self;
    private final ProductService productService;

    @Bean
    ApplicationRunner initDataRunner() {
        return args -> {
            if(productService.count() == 0) {

                // 상품 객체 생성
                Product serrado = new Product("Brazil Serrado Do Capearao", 5000L, 20, "brazil-serrado.jpg");
                Product narino = new Product("Columbia Narino", 5500L, 10, "colombia-narino.jpg");
                Product quindio = new Product("Columbia Quindio", 6000L, 15, "colombia-quindio.jpg");
                Product sidamo = new Product("Ethiopia Sidamo", 6500L, 12, "ethiopia-sidamo.jpg");

                // DB에 저장
                productService.saveAll(List.of(narino, quindio, serrado, sidamo));
            }
        };
    }
}