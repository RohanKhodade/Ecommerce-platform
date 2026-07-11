package com.ecom.inventoryService.exception;

public class JwtTokenNotFound extends RuntimeException {
    public JwtTokenNotFound(String message) {
        super(message);
    }
}
