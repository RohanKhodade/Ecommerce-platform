package com.ecom.userService.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginUserRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String password;

}
