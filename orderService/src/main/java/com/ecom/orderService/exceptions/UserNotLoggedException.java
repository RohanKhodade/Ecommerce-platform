package com.ecom.orderService.exceptions;

public class UserNotLoggedException extends RuntimeException {
    public UserNotLoggedException(String message) {
        super(message);
    }
}
