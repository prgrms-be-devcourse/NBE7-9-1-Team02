package com.back.product.service;

import com.back.product.entity.Product;
import com.back.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }
    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }
    public long count() {
        return productRepository.count();
    }
}
