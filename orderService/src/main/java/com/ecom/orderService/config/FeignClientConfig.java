package com.ecom.orderService.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate->{
            ServletRequestAttributes attributes=
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes !=null ){
                HttpServletRequest request=attributes.getRequest();
                String authHeader=request.getHeader("Authorization");
                if (authHeader!=null && authHeader.startsWith("Bearer ")) {
                    requestTemplate.header("Authorization", authHeader);
                }
            }
        };
    }

    // code to check if feign client outgoing request has headers or not
    @Bean
    public RequestInterceptor debugInterceptor() {
        return template -> {
            System.out.println("Outgoing Feign headers: " + template.headers());
        };
    }

    @Bean
    public CommandLineRunner listInterceptors(ApplicationContext ctx) {
        return args -> {
            String[] names = ctx.getBeanNamesForType(feign.RequestInterceptor.class);
            System.out.println("=== RequestInterceptor beans found: " + names.length + " ===");
            for (String name : names) {
                System.out.println(" -> " + name);
            }
        };
    }
}