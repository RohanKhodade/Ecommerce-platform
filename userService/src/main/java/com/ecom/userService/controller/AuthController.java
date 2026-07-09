package com.ecom.userService.controller;

import com.ecom.userService.dto.request.ChangeEmailRequest;
import com.ecom.userService.dto.request.ChangePasswordRequest;
import com.ecom.userService.dto.request.LoginUserRequest;
import com.ecom.userService.dto.request.RegisterUserRequest;
import com.ecom.userService.dto.response.UserResponse;
import com.ecom.userService.service.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    public AuthController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterUserRequest userReq){
        UserResponse userResponse=userService.registerUser(userReq);
        return new ResponseEntity<> (userResponse,HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginUserRequest loginRequest){
        return new ResponseEntity<>(userService.login(loginRequest),HttpStatus.OK);
    }
    @PutMapping("/update/password/{userId}")
    public ResponseEntity<String> changePassword(@PathVariable Long userId,
                                                 @RequestBody ChangePasswordRequest request){
        return new ResponseEntity<> (userService.changePassword(userId,request),HttpStatus.OK);
    }
    @PutMapping("/update/email/{userId}")
    public ResponseEntity<String> changeEmail(@PathVariable Long userId,
                                              @RequestBody ChangeEmailRequest request){
        return new ResponseEntity<> (userService.changeEmail(userId,request),HttpStatus.OK);
    }
}
