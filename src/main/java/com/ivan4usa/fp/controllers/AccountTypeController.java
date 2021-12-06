package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.constants.MessageTemplates;
import com.ivan4usa.fp.entities.AccountType;
import com.ivan4usa.fp.services.AccountTypeService;
import com.ivan4usa.fp.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The controller that receives requests for operations on AccountType
 */
@Controller
@EnableWebMvc
@CrossOrigin
@RestController
@RequestMapping("/api/account-type")
public class AccountTypeController {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final AccountTypeService service;
    private final UserService userService;

    /**
     * Constructor for class
     * @param service of UserService
     * @param userService of UserService
     */
    @Autowired
    public AccountTypeController(AccountTypeService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    /**
     * Get all account types by user id
     * @param userId id of user
     * @return response with found account types for user
     */
    @PostMapping("/all")
    public ResponseEntity<?> findAll(@RequestBody Long userId) {
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error(MessageTemplates.notMatchMessage(AccountType.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(AccountType.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.findAll(userId));
    }

    /**
     * Get account type by id
     * @param id of account type
     * @return response with found account type
     */
    @PostMapping("/id")
    public ResponseEntity<?> findById(@RequestBody Long id) {
        Long userId = this.userService.getUserId();
        AccountType accountType = null;
        try {
            accountType = service.findById(id).get();
        } catch (NoSuchElementException e) {
            logger.error(MessageTemplates.notFoundMessage(AccountType.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(AccountType.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(accountType.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(AccountType.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(AccountType.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(accountType);
    }

    /**
     * Add new account type
     * @param accountType new account type
     * @return response with saved account type
     */
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody AccountType accountType) {
        Long userId = this.userService.getUserId();
        if (accountType.getId() != null && accountType.getId() != 0) {
            logger.error(MessageTemplates.idMustBeNull(AccountType.class));
            return new ResponseEntity<>(MessageTemplates.idMustBeNull(AccountType.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (accountType.getName() == null ||
                accountType.getUserId() == null) {
            logger.error(MessageTemplates.emptyFields(AccountType.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(AccountType.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(accountType.getUserId(), userId)) {
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(AccountType.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(accountType));
    }

    /**
     * Update existing account type
     * @param accountType existing account type with new data
     * @return response with updated account type
     */
    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestBody AccountType accountType) {
        Long userId = this.userService.getUserId();
        Long id = accountType.getId();
        if (accountType.getId() == null || accountType.getId() == 0) {
            logger.error(MessageTemplates.idIsNull(AccountType.class));
            return new ResponseEntity<>(MessageTemplates.idIsNull(AccountType.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (accountType.getName() == null || accountType.getUserId() == null) {
            logger.error(MessageTemplates.emptyFields(AccountType.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(AccountType.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(accountType.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(AccountType.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(AccountType.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.findById(id).isPresent()) {
            logger.error(MessageTemplates.notFoundMessage(AccountType.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(AccountType.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(accountType));
    }

    /**
     * Delete existing account type
     * @param id of account type that should be deleted
     * @return response with 200 status
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (id == null || id == 0) {
            logger.error(MessageTemplates.missedIdMessage(AccountType.class));
            return new ResponseEntity<>(MessageTemplates.missedIdMessage(AccountType.class), HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error(MessageTemplates.notFoundMessage(AccountType.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(AccountType.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
