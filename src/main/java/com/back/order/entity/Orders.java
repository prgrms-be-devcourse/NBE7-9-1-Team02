package com.back.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Orders {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int orderId;

    private String email;
    private String customerName;
    private String address;
    private int zipcode;
    private int totalPrice;
    private String status = "결제완료";

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @CreatedDate
    private LocalDateTime orderDate;    //주문시간
    private LocalDateTime shippedAt;    //배송시간

    public Orders(String email, String customerName, String address, int zipcode, int totalPrice) {
        this.email = email;
        this.customerName = customerName;
        this.address = address;
        this.zipcode = zipcode;
        this.totalPrice = totalPrice;
    }
}
