package com.ecom.paymentService.controller;

import com.ecom.paymentService.dto.request.MakePaymentRequest;
import com.ecom.paymentService.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @PostMapping("/make")
    public ResponseEntity<Boolean> makePayment(@RequestBody MakePaymentRequest request){
        return new ResponseEntity<> (paymentService.makePayment(request),HttpStatus.OK);
    }
}