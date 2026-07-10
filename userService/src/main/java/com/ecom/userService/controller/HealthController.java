package com.ecom.userService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/health")
public class HealthController {
    @GetMapping
    public ResponseEntity<String> health(){
        return new ResponseEntity<>("App Running...", HttpStatus.OK);
    }
}
