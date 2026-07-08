package com.ecom.userService.service.implementation;

import com.ecom.userService.dto.Mapper;
import com.ecom.userService.dto.request.LoginUserRequest;
import com.ecom.userService.dto.request.RegisterUserRequest;
import com.ecom.userService.dto.response.UserResponse;
import com.ecom.userService.entity.Role;
import com.ecom.userService.entity.User;
import com.ecom.userService.repository.UserRepository;
import com.ecom.userService.service.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           Mapper mapper,
                           PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse registerUser(RegisterUserRequest req) {
        User user = mapper.registerUserRequestToUser(req);
        System.out.println("User:"+ user.toString());
        if (!req.getPassword().equals(req.getConfirmPassword())){
                return null;
            }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.CUSTOMER);
        System.out.println("User:"+ user.toString());
        User savedUser = userRepository.save(user);
        return mapper.UserToUserResponse(savedUser);

    }

    @Override
    public String login(LoginUserRequest req) {
        String email = req.getEmail();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            if (passwordEncoder.matches(req.getPassword(),
                    user.get().getPassword())) {
                return "credentials correct , user logged in. jwt returned";
            }
            return "password incorrect";
        }
        return " no user found with email : " + email;
    }
}