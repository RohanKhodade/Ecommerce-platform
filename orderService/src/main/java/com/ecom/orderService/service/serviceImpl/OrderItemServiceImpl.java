package com.ecom.orderService.service.serviceImpl;

import com.ecom.orderService.repository.OrderItemRepository;
import com.ecom.orderService.service.services.OrderItemService;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository){
        this.orderItemRepository=orderItemRepository;
    }
}
