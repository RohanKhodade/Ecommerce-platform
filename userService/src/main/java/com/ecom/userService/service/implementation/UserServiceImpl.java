package com.ecom.userService.service.implementation;

import com.ecom.userService.dto.Mapper;
import com.ecom.userService.dto.request.*;
import com.ecom.userService.dto.response.UserResponse;
import com.ecom.userService.entity.Role;
import com.ecom.userService.entity.User;
import com.ecom.userService.exception.DuplicateResourceException;
import com.ecom.userService.exception.InvalidCredentialsException;
import com.ecom.userService.exception.ResourceNotFoundException;
import com.ecom.userService.exception.PasswordMismatchException;
import com.ecom.userService.repository.UserRepository;
import com.ecom.userService.service.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        if (userRepository.findByEmail(req.getEmail()).isPresent()){
            throw new DuplicateResourceException("user already exists");
        }
        if (!req.getPassword().equals(req.getConfirmPassword())){
            throw new PasswordMismatchException("enter correct password to confirm ! passwords do not match");
        }
        User user = mapper.registerUserRequestToUser(req);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.CUSTOMER);
        User savedUser = userRepository.save(user);
        return mapper.UserToUserResponse(savedUser);

    }

    @Override
    public String login(LoginUserRequest req) {
        String email = req.getEmail();
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new ResourceNotFoundException("user not found"));
        if (passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return "credentials correct , user logged in. jwt returned";
        }else{
            throw new InvalidCredentialsException("password is incorrect");
        }
    }

    @Override
    public String changePassword(Long userId, ChangePasswordRequest changePassword) {
        User user=userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user not found"));
        String oldPassword=changePassword.getOldPassword();
        String newPassword=changePassword.getNewPassword();
        if (!passwordEncoder.matches(oldPassword,user.getPassword())) {
            throw new InvalidCredentialsException("old password is incorrect");
        }
        if (oldPassword.equals(newPassword)){
            throw new PasswordMismatchException("old and new password should not be same");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "password changed";
    }

    @Override
    public String changeEmail(Long userId, ChangeEmailRequest changeEmail) {
        User user=userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("user not found")
        );
        Optional<User> existingUser=userRepository.findByEmail(changeEmail.getNewEmail());
        if(existingUser.isPresent()){
            throw new DuplicateResourceException("User with "+changeEmail.getNewEmail()
                    +" email already exists");
        }
        user.setEmail(changeEmail.getNewEmail());
        userRepository.save(user);
        return "email changed";
    }

    @Override
    public UserResponse updateUserDetails(Long userId, UpdateUserRequest request){
        User user=userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("user not found")
        );
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBirthdate(LocalDate.parse(request.getBirthDate()));
        user.setPhone(request.getPhone());
        return mapper.UserToUserResponse(userRepository.save(user));
    }

    @Override
    public String deleteUser(Long userId){
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("user not found");
        }
        userRepository.deleteById(userId);
        return "user deleted with id :"+ userId;

    }
}