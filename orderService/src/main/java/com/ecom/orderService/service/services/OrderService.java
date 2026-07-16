package com.ecom.orderService.service.services;

public interface OrderService {
    String checkOut();
    String cancelOrder(Long orderId);
}
