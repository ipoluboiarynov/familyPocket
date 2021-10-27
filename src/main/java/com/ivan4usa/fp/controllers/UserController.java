package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entity.User;
import com.ivan4usa.fp.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

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
