package com.ivan4usa.fp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan4usa.fp.exceptions.JsonException;
import com.ivan4usa.fp.payload.request.LoginRequest;
import com.ivan4usa.fp.payload.request.RegisterRequest;
import com.ivan4usa.fp.security.JWTCookieProvider;
import com.ivan4usa.fp.security.JWTTokenProvider;
import com.ivan4usa.fp.services.CustomUserDetails;
import com.ivan4usa.fp.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JWTTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTCookieProvider jwtCookieProvider;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    public AuthController(JWTTokenProvider jwtTokenProvider, UserService userService,
                          AuthenticationManager authenticationManager, JWTCookieProvider jwtCookieProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtCookieProvider = jwtCookieProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest request) throws JsonProcessingException {
        logger.warn("Hello World");
        // Check login and password
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        // Add info about authentication to the SecurityContextHolder container
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Create an object that is stored in the Spring container and contains user data
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Еhe password is reset to zero before jwt is generated
        userDetails.getUser().setPassword(null);

        // Generate and response generated token with status 200
        String jwt = jwtTokenProvider.generateToken(userDetails.getUser());

        // Create cookie with jwt
        HttpCookie cookie = jwtCookieProvider.createJwtCookie(jwt);

        // Create header for http response and add cookie
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
        httpHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Set-Cookie");

        return ResponseEntity.ok().headers(httpHeaders).body(userDetails.getUser());
    }


    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.userExistsByEmail(request.getEmail())) {
            return new ResponseEntity<>("User with this email already exists." , HttpStatus.NOT_ACCEPTABLE);
        }

        // Validate by email
        if (request.getEmail() == null || request.getEmail().trim().length() == 0) {
            return new ResponseEntity<>("Email is missed.", HttpStatus.NOT_ACCEPTABLE);
        }
        // Validate by password
        if (request.getPassword() == null || request.getPassword().trim().length() == 0 ||
        request.getPassword().length() < 6) {
            return new ResponseEntity<>("Password is not acceptable.", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(userService.createUser(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout() {

        // create expired cookie
        HttpCookie cookie = jwtCookieProvider.deleteJwtCookie();

        // create and set headers with a set cookie
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/update-password")
    public ResponseEntity<Boolean> updatePassword(@RequestBody String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        int updatedRows = userService.updateUserPassword(password, user.getUsername());
        return ResponseEntity.ok(updatedRows == 1);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonException> handleExceptions(Exception e) {
        return new ResponseEntity<>(new JsonException(e.getClass().getSimpleName()), HttpStatus.BAD_REQUEST);
    }
}
