package com.ecom.inventoryService.security;

import com.ecom.inventoryService.exception.UserNotLoggedInException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {


    public Long getLoggedInUserId(){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        if (auth!=null){
            return (Long)auth.getPrincipal();
        }
        throw new UserNotLoggedInException("User not logged in");
    }
}
