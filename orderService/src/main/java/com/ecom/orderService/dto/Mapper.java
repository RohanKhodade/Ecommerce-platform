package com.ecom.orderService.dto;

import com.ecom.orderService.dto.response.CartItemResponse;
import com.ecom.orderService.entity.CartItem;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public CartItem toCartItem(){
        CartItem cartItem = new CartItem();
        return cartItem;
    }

    public CartItemResponse toCartItemResponse(CartItem cartItem){
        CartItemResponse cartItemResponse= new CartItemResponse();
        cartItemResponse.setProductId(cartItem.getProductId());
        cartItemResponse.setQuantity(cartItem.getQuantity());
        cartItemResponse.setCartId(cartItem.getId());
        return cartItemResponse;
    }
}
