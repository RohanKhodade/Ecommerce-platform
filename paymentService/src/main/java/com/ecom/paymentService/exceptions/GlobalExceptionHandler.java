package com.ecom.paymentService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentFailed.class)
    public ResponseEntity<String> handlePaymentFailed(PaymentFailed paymentFailed) {
        return new ResponseEntity<>(paymentFailed.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
