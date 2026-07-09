package com.ecom.userService.service.implementation;

import com.ecom.userService.dto.Mapper;
import com.ecom.userService.dto.request.*;
import com.ecom.userService.dto.response.UserResponse;
import com.ecom.userService.entity.Role;
import com.ecom.userService.entity.User;
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
        User user = mapper.registerUserRequestToUser(req);

        if (userRepository.findByEmail(req.getEmail()).isPresent()){
            return null;
        }

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

    @Override
    public String changePassword(Long userId, ChangePasswordRequest changePassword) {
        User user=userRepository.findById(userId).orElse(null);
        if (user==null) {
            return "no user found with id: " + userId;
        }
        String oldPassword=changePassword.getOldPassword();
        String newPassword=changePassword.getNewPassword();
        if (passwordEncoder.matches(oldPassword,user.getPassword())){
            if (oldPassword.equals(newPassword)){
                return "old and new passwords cannot be same";
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return "password changed";
        }
        return "old password is incorrect";
    }

    @Override
    public String changeEmail(Long userId, ChangeEmailRequest changeEmail) {
        User user=userRepository.findById(userId).orElse(null);
        if (user==null) {
            return "no user found with id: " + userId;
        }
        Optional<User> existingUser=userRepository.findByEmail(changeEmail.getNewEmail());
        if(existingUser.isEmpty()){
            user.setEmail(changeEmail.getNewEmail());
            userRepository.save(user);
            return "email changed";
        }
        return "user with email already exist";
    }

    @Override
    public UserResponse updateUserDetails(Long userId, UpdateUserRequest request){
        User user=userRepository.findById(userId).orElse(null);
        if (user==null){
            return null;
        }
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBirthdate(LocalDate.parse(request.getBirthDate()));
        user.setPhone(request.getPhone());
        return mapper.UserToUserResponse(userRepository.save(user));
    }

    @Override
    public String deleteUser(Long userId){
        if (userRepository.existsById(userId)){
            userRepository.deleteById(userId);
            return "user deleted with id :"+ userId;
        }
        return "user not found";
    }
}