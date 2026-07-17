package com.ecom.orderService.service.services;

import com.ecom.orderService.dto.response.OrderResponse;

public interface OrderService {
    String checkOut();
    String cancelOrder(Long orderId);
    OrderResponse viewOrder(Long orderId);
}
