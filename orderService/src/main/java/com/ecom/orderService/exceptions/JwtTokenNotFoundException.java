package com.ecom.orderService.exceptions;

public class JwtTokenNotFoundException extends RuntimeException {
    public JwtTokenNotFoundException(String message) {
        super(message);
    }
}
