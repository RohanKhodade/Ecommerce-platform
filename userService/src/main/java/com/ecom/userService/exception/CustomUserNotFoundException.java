package com.ecom.userService.exception;

public class CustomUserNotFoundException extends RuntimeException{
    public CustomUserNotFoundException(String message){
        super(message);
    }
}
