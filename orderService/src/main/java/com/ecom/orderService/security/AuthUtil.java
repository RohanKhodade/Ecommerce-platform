package com.ecom.orderService.security;

import com.ecom.orderService.exceptions.UserNotLoggedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    public Long getLoggedInUserId(){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        if (auth!=null){
            return (Long) auth.getPrincipal();
        }
        throw new UserNotLoggedException("User not logged !");
    }
}
