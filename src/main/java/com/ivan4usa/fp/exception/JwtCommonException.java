package com.ivan4usa.fp.exception;


import org.springframework.security.core.AuthenticationException;

public class JwtCommonException extends AuthenticationException {

    public JwtCommonException(String explanation) {
        super(explanation);
    }

    public JwtCommonException(String explanation, Throwable throwable) {
        super(explanation, throwable);
    }
}
