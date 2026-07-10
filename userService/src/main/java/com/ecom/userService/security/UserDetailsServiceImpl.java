package com.ecom.userService.security;

import com.ecom.userService.entity.User;
import com.ecom.userService.exception.CustomUserNotFoundException;
import com.ecom.userService.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Bridges YOUR User entity/DB table to what Spring Security actually understands (UserDetails).
    // Spring Security has no idea what your "User" entity looks like — this class translates.
    // Called in TWO places: once during login (by DaoAuthenticationProvider), and once
    // per-request inside JwtAuthFilter (to reload fresh user info based on the token's username).
    private final UserRepository userRepository;
    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(@NonNull String userEmail){
        // fresh DB lookup — never trust stale data baked into a token
        User user=userRepository.findByEmail(userEmail).orElseThrow(
                ()-> new CustomUserNotFoundException("user with email not found: " + userEmail));

        // wrap your entity into Spring's built-in UserDetails object
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                // stored BCrypt hash, used ONLY at login to compare — ignored afterwards since JWT already proves identity
                .roles(user.getRole().name().replace("ROLE_",""))
                // Spring auto-prepends "ROLE_" internally, so strip it here to avoid double-prefixing
                .build();
    }
}
