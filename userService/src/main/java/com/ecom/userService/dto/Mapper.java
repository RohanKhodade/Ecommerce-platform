package com.ecom.userService.dto;

import com.ecom.userService.dto.request.RegisterUserRequest;
import com.ecom.userService.dto.response.UserResponse;
import com.ecom.userService.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Mapper {

    public User registerUserRequestToUser(RegisterUserRequest request){
        User user=new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setBirthdate(LocalDate.parse(request.getBirthdate()));
        user.setPhone(request.getPhone());
        return user;
    }

    public UserResponse UserToUserResponse(User user){
        UserResponse userResponse=new UserResponse();
        userResponse.setId(user.getId().toString());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setBirthdate(user.getBirthdate().toString());
        return userResponse;
    }
}
