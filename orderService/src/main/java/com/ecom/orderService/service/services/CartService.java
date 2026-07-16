package com.ecom.orderService.service.services;

import com.ecom.orderService.dto.requests.CartItemRequest;
import com.ecom.orderService.dto.response.CartItemResponse;
import com.ecom.orderService.entity.Cart;
import com.ecom.orderService.entity.CartItem;

import java.util.List;

public interface CartService {

    String addItemToCart(CartItemRequest request);
    String removeItemFromCart(Long itemId);
    String updateCartItemQuantity(CartItemRequest request);
    List<CartItemResponse> getCartItems();
    Cart getCart();
    String clearCart();
    CartItemResponse getCartItem(Long itemId);
//    Integer getTotalPriceOfCart();
}