package com.ecom.userService.service.services;

import com.ecom.userService.dto.request.LoginUserRequest;
import com.ecom.userService.dto.request.RegisterUserRequest;
import com.ecom.userService.dto.response.UserResponse;
import com.ecom.userService.entity.User;

public interface UserService {

    UserResponse registerUser(RegisterUserRequest registerUserRequest);
    String login(LoginUserRequest loginRequest);
}