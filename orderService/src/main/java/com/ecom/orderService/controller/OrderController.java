package com.ecom.orderService.controller;

import com.ecom.orderService.service.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
