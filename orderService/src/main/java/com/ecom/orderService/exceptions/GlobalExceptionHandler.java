package com.ecom.orderService.exceptions;

import com.ecom.orderService.dto.response.ErrorResponse;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtTokenNotFoundException.class)
    public ResponseEntity<String> handleJwtTokenNotFoundException(JwtTokenNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotLoggedException.class)
    public ResponseEntity<String> handleUserNotLoggedException(
            UserNotLoggedException ex){
        return new ResponseEntity<> (ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<> (ex.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<> (ex.getMessage(),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<> (ex.getMessage(),HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException.NotFound ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Requested product not found :"+ ex.getMessage()));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex){
        return new ResponseEntity<>(
                new ErrorResponse("Feign Error: "+ ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
