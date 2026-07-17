package com.ecom.userService.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthFilter(JwtUtility jwtUtility,
                         UserDetailsServiceImpl userDetailsService) {
        this.jwtUtility = jwtUtility;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterchain) throws ServletException, IOException {

        // 1. get Authentication header from request
        String authHeader=request.getHeader("Authorization");
        System.out.println("authHeader:"+authHeader);
        String token=null;
        String userEmail=null;

        //2. check if header has a bearer token
        if (authHeader!=null && authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);
            // raw token is ahead of bearer token
            try{
                userEmail=jwtUtility.validateToken(token);
                //validates expiry signature then pulls out the email
            }catch(ExpiredJwtException e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "Session expired login again");
            }catch(Exception e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid token");
            }
        }
        // NOTE: if there was no Bearer header at all, username stays null and we just fall through —
        // this request proceeds unauthenticated, and gets blocked later ONLY if the endpoint
        // //requires auth
        // (that blocking happens in Spring's built-in FilterSecurityInterceptor,
        // after this filter, based on SecurityConfig's rules)

        //3. if userEmail is found and security context holder is not set then set it
        if (userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            //reload the user fresh from db rather than trusting the token
            UserDetails userDetails=userDetailsService.loadUserByUsername(userEmail);
            // till now we have verified the token and extracted the user from the db
            // the main part is that we need to tell spring that this request is authenticated
            // by putting the authenticated user in spring security context
            UsernamePasswordAuthenticationToken authToken=
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                    );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // continue the filter chain - request moves to next filter and then eventually controller
        filterchain.doFilter(request,response);
    }
}