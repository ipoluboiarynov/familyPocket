package com.ivan4usa.fp.exception;


import org.springframework.security.core.AuthenticationException;

public class JwtNotValidException extends AuthenticationException {

    public JwtNotValidException(String explanation) {
        super(explanation);
    }

    public JwtNotValidException(String explanation, Throwable throwable) {
        super(explanation, throwable);
    }
}
