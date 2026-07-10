package com.ecom.userService.security;

import com.ecom.userService.entity.User;
import com.ecom.userService.exception.ResourceNotFoundException;
import com.ecom.userService.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserAccessGuard {

    private final AuthenticationUtil authUtil;
    private final UserRepository userRepository;
    public UserAccessGuard(AuthenticationUtil authUtil,
                           UserRepository userRepository) {
        this.authUtil = authUtil;
        this.userRepository = userRepository;
    }

    public User getVerifiedUser(Long userId){
        User user=userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User not found with id: " + userId));
        authUtil.checkOwnerShip(user.getEmail());
        return user;
    }
}
