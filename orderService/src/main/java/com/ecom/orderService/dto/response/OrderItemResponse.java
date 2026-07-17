package com.ecom.orderService.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {

    private Long productId;
    private String productName;
    private BigDecimal PriceAtPurchase;
    private Integer quantity;
    private BigDecimal subtotal;
    private Long orderId;
    private String itemStatus;
    private Long sellerId;
}
