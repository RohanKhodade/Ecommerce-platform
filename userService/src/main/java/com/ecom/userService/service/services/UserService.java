package com.ecom.userService.service.services;

import com.ecom.userService.dto.request.*;
import com.ecom.userService.dto.response.UserResponse;

public interface UserService {

    UserResponse registerUser(RegisterUserRequest registerUserRequest);
    String login(LoginUserRequest loginRequest);
    String changePassword(Long userId, ChangePasswordRequest changePassword);
    String changeEmail(Long userId, ChangeEmailRequest changeEmail);
    UserResponse updateUserDetails(Long userId, UpdateUserRequest request);
    String deleteUser(Long userId);
    UserResponse getMyInfo();
}