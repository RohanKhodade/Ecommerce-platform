package com.ecom.userService.security;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


// this is the master rule book of spring security
// spring builds this once at startup and applies it to every request after that
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          JwtAuthFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity.
                // for production apply the cors method here
                csrf(csrf->csrf.disable())
                .authorizeHttpRequests(authorizeRequests->
                        authorizeRequests.requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/app/health"
                                ).permitAll()
                                .anyRequest().authenticated())
                // no http sessions each request will prove itself via jwt
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // insert out jwt authentication before springs UsernameAndPasswordAuthentication
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    // used to hash and match the passwords
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // this bean is what actually runs the login check (used inside your AuthController)
    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) throws Exception{
        //Dao authenticationProvider : loads user from db via UserDetailsServiceImpl
        // then compare password hash
        DaoAuthenticationProvider daoAuth=new DaoAuthenticationProvider(userDetailsService);
        daoAuth.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(daoAuth);
    }

    // browsers block cross-origin requests by default — this whitelist tells the browser
    // "these frontend origins are allowed to call this API with credentials"
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("",""));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
