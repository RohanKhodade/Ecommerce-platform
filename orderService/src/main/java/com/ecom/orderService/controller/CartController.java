package com.ecom.orderService.controller;

import com.ecom.orderService.dto.requests.CartItemRequest;
import com.ecom.orderService.dto.response.CartItemResponse;
import com.ecom.orderService.entity.CartItem;
import com.ecom.orderService.service.services.CartService;
import jakarta.ws.rs.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService){
        this.cartService=cartService;
    }

    @GetMapping("/")
    public ResponseEntity<List<CartItemResponse>> getCart(){
        return new ResponseEntity<> (cartService.getCartItems(),HttpStatus.OK);
    }
    @GetMapping("/view/{productId}")
    public ResponseEntity<CartItemResponse> getCartItem(@PathVariable Long productId){
        return new ResponseEntity<>(cartService.getCartItem(productId),HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartItemRequest request){
        return new ResponseEntity<> (cartService.addItemToCart(request),HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateCartItem(@RequestBody CartItemRequest request){
        return new ResponseEntity<> (cartService.updateCartItemQuantity(request),HttpStatus.OK);
    }
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Long productId){
        return new ResponseEntity<>(cartService.removeItemFromCart(productId),HttpStatus.OK);
    }
    @DeleteMapping("/clearCart")
    public ResponseEntity<String> clearCart(){
        return new ResponseEntity<>(cartService.clearCart(),HttpStatus.OK);
    }
}