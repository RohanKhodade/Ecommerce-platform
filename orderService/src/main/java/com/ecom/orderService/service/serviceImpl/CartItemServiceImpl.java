package com.ecom.orderService.service.serviceImpl;

import com.ecom.orderService.repository.CartItemRepository;
import com.ecom.orderService.service.services.CartItemService;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }
}
