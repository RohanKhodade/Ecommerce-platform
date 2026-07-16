package com.ecom.orderService.dto.response;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class CartItemResponse {

    private Long productId;
    private Integer quantity;
    private Long cartId;
}
