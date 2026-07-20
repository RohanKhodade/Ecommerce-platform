package com.ecom.inventoryService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex){
        return new ResponseEntity<> (ex.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtTokenNotFound.class)
    public ResponseEntity<String> handleJwtTokenNotFound(JwtTokenNotFound ex){
        return new ResponseEntity<> (ex.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidJwtToken.class)
    public ResponseEntity<String> handleInvalidJwtToken(InvalidJwtToken ex){
        return new ResponseEntity<> (ex.getMessage(),HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex){
        return new ResponseEntity<> (ex.getMessage(),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("errors", fieldErrors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<String> handleInsufficientStockException(InsufficientStockException ex){
        return new ResponseEntity<> (ex.getMessage(),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserNotLoggedInException.class)
    public ResponseEntity<String> handleUserNotLoggedInException(UserNotLoggedInException ex){
        return new ResponseEntity<> (ex.getMessage(),HttpStatus.UNAUTHORIZED);
    }
}
