package com.ecom.orderService.controller;

import com.ecom.orderService.dto.response.OrderResponse;
import com.ecom.orderService.service.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    public  OrderController(OrderService orderService){
        this.orderService=orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<String> checkout(){
        return new ResponseEntity<>(orderService.checkOut(), HttpStatus.OK);
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancel(@PathVariable Long orderId){
        return new ResponseEntity<>(orderService.cancelOrder(orderId),HttpStatus.OK);
    }

    @GetMapping("/view/{orderId}")
    public ResponseEntity<OrderResponse> viewOrder(@PathVariable Long orderId){
        return new ResponseEntity<>(orderService.viewOrder(orderId),HttpStatus.OK);
    }
}
