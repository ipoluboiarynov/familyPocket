package com.ivan4usa.fp.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterRequest {

    @Email(message = "Email format accepted only.")
    @NotBlank(message = "Email is required.")
    private String email;

    @NotEmpty(message = "Password is required.")
    @Size(min = 6)
    private String password;

    private String name;
}
