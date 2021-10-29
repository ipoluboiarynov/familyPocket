package com.ivan4usa.fp.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserExistsException extends AuthenticationException {

    public UserExistsException(String explanation) {
        super(explanation);
    }

    public UserExistsException(String explanation, Throwable throwable) {
        super(explanation, throwable);
    }
}
