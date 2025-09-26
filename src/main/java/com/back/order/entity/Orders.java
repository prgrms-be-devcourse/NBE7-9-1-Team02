package com.back.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "`order`")
@Getter
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int orderId;

    private String email;
    private int total_price;
    private String status;

    @CreatedDate
    private LocalDateTime order_date;
}
