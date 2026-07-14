package com.ecom.orderService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @Column(nullable=false)
    private Long productId;

    @Column(nullable=false)
    private String productName;

    @Column(nullable=false)
    private Long sellerId;

    @Column(nullable=false)
    private BigDecimal priceAtPurchase;

    @Column(nullable=false)
    private Integer quantity;

    @Column(nullable=false)
    private BigDecimal subTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private ItemStatus status;

    private LocalDateTime orderDate;

    @PrePersist
    public void onOrder(){
        this.orderDate = LocalDateTime.now();
    }
}
