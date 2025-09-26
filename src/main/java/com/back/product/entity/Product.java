package com.back.product.entity;

import com.back.order.entity.OrderProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    private String name;
    private int price;
    private int quantity;   // 재고
    private String photoUrl;

    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ProductDetail productDetail;

    public Product(String name, int price, int quantity, String photoUrl) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.photoUrl = photoUrl;
    }
}