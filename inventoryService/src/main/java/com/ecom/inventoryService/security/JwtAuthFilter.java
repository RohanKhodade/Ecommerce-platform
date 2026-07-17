package com.ecom.inventoryService.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;
    public JwtAuthFilter(JwtUtility jwtUtility) {
        this.jwtUtility = jwtUtility;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterchain)throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        System.out.println("authHeader:"+authHeader);
        String token=null;
        Long userId=null;

        if (authHeader!=null && authHeader.startsWith("Bearer ")) {
            token=authHeader.substring(7);
            System.out.println("Auth Header:"+authHeader);
            try{
                userId=jwtUtility.extractUserIdFromToken(token);
            }catch(ExpiredJwtException e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"token expired");
            }catch(Exception e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"token invalid");
            }
        }

        if (userId!=null && SecurityContextHolder.getContext().getAuthentication()==null){

                UsernamePasswordAuthenticationToken authToken=
                        new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                Collections.emptyList()
                        );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                // to set details :request ip address from where request arrives and session Id
        }
        filterchain.doFilter(request,response);

    }

}
