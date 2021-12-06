package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.constants.MessageTemplates;
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
    public ResponseEntity<?> findById(@RequestBody Long id) {
        Long userId = this.service.getUserId();
        User user;
        try {
            user = service.getUserById(id).get();
        } catch (NoSuchElementException e) {
            logger.error(MessageTemplates.notFoundMessage(User.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(User.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(id, userId)) {
            logger.error(MessageTemplates.notMatchMessage(User.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(User.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Update existing user data
     * @param user to be updated
     * @return response with updated user
     */
    @PostMapping("/update")
    public ResponseEntity<?> findById(@RequestBody User user) {
        Long userId = this.service.getUserId();
        Long id = user.getId();
        if (id == null || id == 0) {
            logger.error(MessageTemplates.idIsNull(User.class));
            return new ResponseEntity<>(MessageTemplates.idIsNull(User.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (user.getEmail() == null ) {
            logger.error(MessageTemplates.emptyFields(User.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(User.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(id, userId)) {
            logger.error(MessageTemplates.notMatchMessage(User.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(User.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.getUserById(id).isPresent()) {
            logger.error(MessageTemplates.notFoundMessage(User.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(User.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.updateUser(user));
    }
}
