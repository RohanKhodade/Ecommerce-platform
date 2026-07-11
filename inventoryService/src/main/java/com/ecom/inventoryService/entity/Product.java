package com.ecom.inventoryService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal rating;

    @JoinColumn(name="category_id",nullable=false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private Long sellerId;
}
