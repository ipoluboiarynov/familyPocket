package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.payload.request.LoginRequest;
import com.ivan4usa.fp.payload.request.RegisterRequest;
import com.ivan4usa.fp.security.JWTTokenProvider;
import com.ivan4usa.fp.security.SecurityConstants;
import com.ivan4usa.fp.services.UserService;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody LoginRequest request, BindingResult result) {
        // Check login and password
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        // Add info about authentication to the SecurityContextHolder container
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate and response generated token with status 200
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }


    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterRequest request, BindingResult result) {
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
}
