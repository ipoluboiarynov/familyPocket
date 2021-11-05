package com.ivan4usa.fp.security;

import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class JWTCookieProvider {

    @Value("${jwt.cookie.name}")
    private String access_token;

    @Value("${jwt.cookie.max_age}")
    private int cookieAccessTokenDuration;

    @Value("${cookie.address}")
    private String cookieAccessTokenDomain;

    /**
     * Creates server-side cookie with jwt value. Only for server reading (not client)
     * @param jwt json web token
     * @return cookie with jwt
     */
    public HttpCookie createJwtCookie(String jwt) {
        return ResponseCookie
                .from(access_token, jwt) // name and value of cookie
                .maxAge(cookieAccessTokenDuration) // max age of cookie
                .sameSite(SameSiteCookies.STRICT.getValue()) // protection from requests from third-party sites (CSRF-attacks)
                .httpOnly(true) // cookie is readable only on server (XSS-attacks)
                .secure(true) // only for https protocols of requests
                .domain(cookieAccessTokenDomain) // domain that acceptable for cookie
                .path("/") // cookie is valid for any page of domain
                .build();  // create cookie
    }

    /**
     * Method looking for access token in cookies of request and returns its value
     * @param request http request
     * @return jwt value of cookie or null
     */
    public String getCookieAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                if (access_token.equals(cookie.getName()) &&
                        StringUtils.hasText(cookie.getValue())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Method sets cookie with max age of 0
     * @return cookie with max age of 0 and value of null
     */
    public HttpCookie deleteJwtCookie() {
        return ResponseCookie
                .from(access_token, null) // set value to null
                .maxAge(0) // if maxAge is 0 browser automatically deletes cookie
                .sameSite(SameSiteCookies.STRICT.getValue())
                .httpOnly(true)
                .secure(true)
                .domain(cookieAccessTokenDomain)
                .path("/")
                .build();
    }

}
