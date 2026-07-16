package com.ecom.orderService.dto.response;


import com.ecom.orderService.entity.CartItem;
import lombok.Data;

import java.util.List;

@Data
public class CartResponse {

    private Long cartId;
    private List<CartItemResponse> cartItems;
}
