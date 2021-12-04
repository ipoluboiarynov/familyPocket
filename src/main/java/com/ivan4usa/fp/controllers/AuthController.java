package com.ivan4usa.fp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan4usa.fp.exceptions.JsonException;
import com.ivan4usa.fp.payload.request.LoginRequest;
import com.ivan4usa.fp.payload.request.RegisterRequest;
import com.ivan4usa.fp.payload.response.JWTSuccessResponse;
import com.ivan4usa.fp.security.JWTTokenProvider;
import com.ivan4usa.fp.services.CustomUserDetails;
import com.ivan4usa.fp.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.Valid;

/**
 * The controller that receives requests for operations with authentication
 */
@Controller
@EnableWebMvc
@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JWTTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.auth.token_prefix}")
    private String TOKEN_PREFIX;

    /**
     * Constructor for class
     * @param jwtTokenProvider of JWTTokenProvider
     * @param userService of UserService
     * @param authenticationManager of AuthenticationManager
     */
    @Autowired
    public AuthController(JWTTokenProvider jwtTokenProvider, UserService userService,
                          AuthenticationManager authenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * The method that receives the data with credentials, checks it and responses with generated jwt and with user id
     * @param request with credentials (email address and password)
     * @return response with jwt and user id
     * @throws JsonProcessingException exception processing the json conversion
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest request) throws JsonProcessingException {

        // Check login and password
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        // Add info about authentication to the SecurityContextHolder container
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Create an object that is stored in the Spring container and contains user data
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Set password to zero before jwt is generated
        userDetails.getUser().setPassword(null);

        // Generate and response generated token with status 200
        String jwt = TOKEN_PREFIX + jwtTokenProvider.generateToken(userDetails.getUser());

        // Get user id to return it to frontend
        Long id = userDetails.getUser().getId();

        return ResponseEntity.ok(new JWTSuccessResponse(true, jwt, id));
    }


    /**
     * The method that receives new credentials, checks it for unique and returns response with new userr
     * @param request credentials (email address and password)
     * @return response with new user
     */
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.userExistsByEmail(request.getEmail())) {
            return new ResponseEntity<>("User with this email already exists.", HttpStatus.NOT_ACCEPTABLE);
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

    /**
     * The method that updates password for user by user id
     * @param password new password
     * @return response with updated rows
     */
    @PostMapping("/update-password")
    public ResponseEntity<Boolean> updatePassword(@RequestBody String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        int updatedRows = userService.updateUserPassword(password, user.getUsername());
        return ResponseEntity.ok(updatedRows == 1);
    }

    /**
     * Method that catches and handles bad requests
     * @param e exception
     * @return response with simple name of exception and status of bad request
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonException> handleExceptions(Exception e) {
        return new ResponseEntity<>(new JsonException(e.getClass().getSimpleName()), HttpStatus.BAD_REQUEST);
    }
}
