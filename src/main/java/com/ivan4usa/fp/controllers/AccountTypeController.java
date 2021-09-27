package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entity.AccountType;
import com.ivan4usa.fp.services.AccountTypeService;
import com.ivan4usa.fp.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/api/account-type")
public class AccountTypeController {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final AccountTypeService service;
    private final UserService userService;

    @Autowired
    public AccountTypeController(AccountTypeService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping("/all")
    public ResponseEntity<List<AccountType>> findAll(@RequestBody Long userId) {
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error("Account type is not match to user id of " + userId);
            return new ResponseEntity("Account type is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.findAll(userId));
    }

    @PostMapping("/id")
    public ResponseEntity<AccountType> findById(@RequestBody Long id) {
        Long userId = this.userService.getUserId();
        AccountType accountType = null;
        try {
            accountType = service.findById(id).get();
        } catch (NoSuchElementException e) {
            logger.error("Account type with id = " + id + " not found");
            return new ResponseEntity("Account type not found", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(accountType.getUserId(), userId)) {
            logger.error("Account type is not match to user id of " + id);
            return new ResponseEntity("Account type is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(accountType);
    }

    @PostMapping("/add")
    public ResponseEntity<AccountType> add(@RequestBody AccountType accountType) {
        Long userId = this.userService.getUserId();
        if (accountType.getId() != null && accountType.getId() != 0) {
            logger.error("Account type id must be null");
            return new ResponseEntity("Account type id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (accountType.getName() == null ||
                accountType.getUserId() == null) {
            logger.error("Some fields of account type are empty");
            return new ResponseEntity("Some fields of account type are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(accountType.getUserId(), userId)) {
            return new ResponseEntity("Account type is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(accountType));
    }

    @PatchMapping("/update")
    public ResponseEntity<AccountType> update(@RequestBody AccountType accountType) {
        Long userId = this.userService.getUserId();
        Long id = accountType.getId();
        if (accountType.getId() == null || accountType.getId() == 0) {
            logger.error("Account type id is null");
            return new ResponseEntity("Account type id is null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (accountType.getName() == null || accountType.getUserId() == null) {
            logger.error("Some fields of account type are empty");
            return new ResponseEntity("Some fields of account type are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(accountType.getUserId(), userId)) {
            return new ResponseEntity("Account type is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.findById(id).isPresent()) {
            logger.error("Account type id" + id + " is not found");
            return new ResponseEntity("Account type id " + id + " is not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(accountType));
    }

    @DeleteMapping("delete")
    public ResponseEntity<AccountType> delete(@RequestBody AccountType accountType) {
        Long userId = this.userService.getUserId();
        Long id = accountType.getId();

        if (id == null || id == 0) {
            return new ResponseEntity("Account type id missed", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(accountType.getUserId(), userId)) {
            return new ResponseEntity("Account type is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Account type id "+ id + " not found");
            return new ResponseEntity("Account type id " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
