package com.ecom.userService.security;

import com.ecom.userService.exception.AccessDeniedBySecurityException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtil {
    // this class is for checking if current logged in user is accessing his own details only

    public String getLoggedInUser(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    public void checkOwnerShip(String userEmail){
        String loggedInUserEmail=getLoggedInUser();
        if (!loggedInUserEmail.equals(userEmail)){
            throw new AccessDeniedBySecurityException("You can access only your data");
        }
    }
}