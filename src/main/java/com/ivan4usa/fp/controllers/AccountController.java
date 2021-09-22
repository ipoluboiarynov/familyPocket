package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entity.Account;
import com.ivan4usa.fp.services.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final AccountService service;

    @Autowired
    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Account>> findAll(@RequestBody Long userId) {
        return ResponseEntity.ok(service.findAll(userId));
    }

    @PostMapping("/id")
    public ResponseEntity<Account> findById(@RequestBody Long id) {
        Account account = null;
        try {
            account = service.findById(id);
        } catch (NoSuchElementException e) {
            logger.error("account with id = " + id + " not found");
            return new ResponseEntity("account not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/add")
    public ResponseEntity<Account> add(@RequestBody Account account) {
        if (account.getId() != null || account.getId() != 0) {
            logger.error("account id must be null");
            return new ResponseEntity("account id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (account.getName() == null ||
        account.getColor() == null ||
        account.getIcon() == null ||
        account.getCreditLimit() == null ||
        account.getStartBalance() == null ||
        account.getAccountType() == null ||
        account.getUserId() == null ||
        account.getCurrency() == null) {
            logger.error("Some fields of account are empty");
            return new ResponseEntity("Some fields of account are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(account));
    }

    @PatchMapping("/update")
    public ResponseEntity<Account> update(@RequestBody Account account) {
        if (account.getId() == null || account.getId() == 0) {
            logger.error("account id is null");
            return new ResponseEntity("account id is null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (account.getName() == null ||
                account.getColor() == null ||
                account.getIcon() == null ||
                account.getCreditLimit() == null ||
                account.getStartBalance() == null ||
                account.getAccountType() == null ||
                account.getUserId() == null ||
                account.getCurrency() == null) {
            logger.error("Some fields of account are empty");
            return new ResponseEntity("Some fields of account are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(account));
    }

    @DeleteMapping("delete")
    public ResponseEntity<Account> delete(@RequestBody Long id) {
        if (id == null || id == 0) {
            return new ResponseEntity("id missed", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("account id "+ id + " not found");
            return new ResponseEntity("account id " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
