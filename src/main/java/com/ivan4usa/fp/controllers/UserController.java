package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entities.User;
import com.ivan4usa.fp.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The controller that receives requests for operations on User
 */
@Controller
@EnableWebMvc
@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final UserService service;

    /**
     * Constructor for class
     * @param service of UserService
     */
    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * Get user by id
     * @param id of user
     * @return response with found user or with error message
     */
    @PostMapping("/id")
    public ResponseEntity<User> findById(@RequestBody Long id) {
        Long userId = this.service.getUserId();
        User user = null;
        try {
            user = service.getUserById(id).get();
        } catch (NoSuchElementException e) {
            logger.error("User with id = " + id + " not found");
            return new ResponseEntity("User not found", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(id, userId)) {
            logger.error("User is not match to user id of " + id);
            return new ResponseEntity("User is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Update existing user data
     * @param user to be updated
     * @return response with updated user
     */
    @PostMapping("/update")
    public ResponseEntity<Integer> findById(@RequestBody User user) {
        Long userId = this.service.getUserId();
        Long id = user.getId();
        if (id == null && id == 0) {
            logger.error("User id is null");
            return new ResponseEntity("User id is null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (user.getEmail() == null ) {
            logger.error("Some fields of user are empty");
            return new ResponseEntity("Some fields of user are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(id, userId)) {
            return new ResponseEntity("User is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.getUserById(id).isPresent()) {
            logger.error("User id" + id + " is not found");
            return new ResponseEntity("User id " + id + " is not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.updateUser(user));
    }
}
