package com.ivan4usa.fp.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LoginRequest {
    @NotEmpty(message = "Username cannot be empty.")
    private String username;

    @NotEmpty(message = "Password cannot be empty.")
    private String password;
}
