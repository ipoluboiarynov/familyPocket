package com.ivan4usa.fp.security;

public class SecurityConstants {
    public static final String SIGN_UP_URLS = "/api/auth/**";
    public static final String SECRET_KEY = "SecretKeyForJWT";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
    public static final int SHORT_EXPIRATION_TIME = 2 * 60 * 60 * 1000; // 2 hours
    public static final int LONG_EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 hours

}
