package com.back.admin.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor // JPA용 기본 생성자
@Entity
@Table(name = "orders") // order가 MySQL 예약어라서 이름 바꿔주는게 안전
public class Order { // 주문 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Integer id;                // 주문 번호


    @Column(name = "order_date")
    private LocalDateTime orderDate;// 주문 일시

    @Enumerated(EnumType.STRING)
    private OrderStatus status;     // 주문 상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts; // 주문 상품

    @Column(name = "total_price")
    private Long totalPrice;

    // 배송 시작 시각 (관리자가 '배송하기' 클릭한 시점)
    @Column(name = "shipped_at")
    private OffsetDateTime shippedAt;

    @Column(name = "address")
    private String address;    // 주소

    @Column(name = "zipcode")
    private Integer zipcode;   // 우편 번호

    private String email;           // 고객 이메일

    @Column(name = "customer_name")
    private String customerName;   // 주문자 명
}

