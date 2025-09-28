package com.back.domain.order.entity;

import com.back.domain.order.entity.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "address")
    private String address;

    @Column(name = "zipcode")
    private Integer zipcode;

    @Column(name = "total_price")
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // Enum으로 통일

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @CreatedDate
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    public Orders(String email, String customerName, String address, Integer zipcode, Long totalPrice) {
        this.email = email;
        this.customerName = customerName;
        this.address = address;
        this.zipcode = zipcode;
        this.totalPrice = totalPrice;
    }
}
