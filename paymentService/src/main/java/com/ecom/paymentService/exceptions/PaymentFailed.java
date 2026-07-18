package com.ecom.paymentService.exceptions;

public class PaymentFailed extends RuntimeException {
    public PaymentFailed(String message) {
        super(message);
    }
}
