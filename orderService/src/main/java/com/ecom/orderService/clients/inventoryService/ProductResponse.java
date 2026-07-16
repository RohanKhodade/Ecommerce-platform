package com.ecom.orderService.clients.inventoryService;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private String productId;
    private String name;
    private String description;
    private String quantity;
    private BigDecimal price;
    private String categoryName;
    private String sellerId;
}