package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entity.Account;
import com.ivan4usa.fp.services.AccountService;
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
@RequestMapping("/api/account")
public class AccountController {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final AccountService service;
    private final UserService userService;

    @Autowired
    public AccountController(AccountService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Account>> findAll(@RequestBody Long userId) {
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error("Account is not match to user id of " + userId);
            return new ResponseEntity("Account is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.findAll(userId));
    }

    @PostMapping("/id")
    public ResponseEntity<Account> findById(@RequestBody Long id) {
        Long userId = this.userService.getUserId();
        Account account = null;
        try {
            account = service.findById(id).get();
        } catch (NoSuchElementException e) {
            logger.error("Account with id = " + id + " not found");
            return new ResponseEntity("Account not found", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(account.getUserId(), userId)) {
            logger.error("Account is not match to user id of " + id);
            return new ResponseEntity("Account is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/add")
    public ResponseEntity<Account> add(@RequestBody Account account) {
        Long userId = this.userService.getUserId();
        if (account.getId() != null && account.getId() != 0) {
            logger.error("Account id must be null");
            return new ResponseEntity("Account id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (account.getName() == null ||
        account.getColor() == null ||
        account.getIcon() == null ||
        account.getCreditLimit() == null ||
        account.getStartBalance() == null ||
        account.getUserId() == null) {
            logger.error("Some fields of account are empty");
            return new ResponseEntity("Some fields of account are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(account.getUserId(), userId)) {
            return new ResponseEntity("Account is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(account));
    }

    @PatchMapping("/update")
    public ResponseEntity<Account> update(@RequestBody Account account) {
        Long userId = this.userService.getUserId();
        Long id = account.getId();
        if (account.getId() == null && account.getId() == 0) {
            logger.error("Account id is null");
            return new ResponseEntity("Account id is null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (account.getName() == null ||
                account.getColor() == null ||
                account.getIcon() == null ||
                account.getCreditLimit() == null ||
                account.getStartBalance() == null ||
                account.getUserId() == null) {
            logger.error("Some fields of account are empty");
            return new ResponseEntity("Some fields of account are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(account.getUserId(), userId)) {
            return new ResponseEntity("Account is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.findById(id).isPresent()) {
            logger.error("Account id" + id + " is not found");
            return new ResponseEntity("Account id " + id + " is not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(account));
    }

    @DeleteMapping("delete")
    public ResponseEntity<Account> delete(@RequestBody Account account) {
        Long userId = this.userService.getUserId();
        Long id = account.getId();

        if (id == null || id == 0) {
            return new ResponseEntity("Account id missed", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(account.getUserId(), userId)) {
            return new ResponseEntity("Account is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Account id "+ id + " not found");
            return new ResponseEntity("Account id " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
