package com.ecom.userService.exception;

public class AccessDeniedBySecurityException extends RuntimeException {
    public AccessDeniedBySecurityException(String message){
        super(message);
    }
}
