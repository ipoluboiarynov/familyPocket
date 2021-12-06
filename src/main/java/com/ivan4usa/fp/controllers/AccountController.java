package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.constants.MessageTemplates;
import com.ivan4usa.fp.entities.Account;
import com.ivan4usa.fp.services.AccountService;
import com.ivan4usa.fp.services.UserService;
import com.ivan4usa.fp.wrappers.IdAndDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The controller that receives requests for operations on Account
 */
@Controller
@EnableWebMvc
@CrossOrigin
@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final AccountService service;
    private final UserService userService;

    /**
     * Constructor for class
     * @param service of AccountService
     * @param userService of UserService
     */
    @Autowired
    public AccountController(AccountService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    /**
     * Get All accounts by userId and date until which the balance is calculated
     * @param obj with user id and the end date for calculating the balance
     * @return response with list of found accounts
     */
    @PostMapping("/all")
    public ResponseEntity<?> findAll(@RequestBody IdAndDate obj) {
        Long userId = obj.getId();
        String dateString = obj.getDate();
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error(MessageTemplates.notMatchMessage(Account.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Account.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.findAll(userId, dateString));
    }

    /**
     * Get curtain account by id and date until which the balance is calculated
     * @param obj with account id and the end date for calculating the balance
     * @return response with found Account
     */
    @PostMapping("/id")
    public ResponseEntity<?> findById(@RequestBody IdAndDate obj) {
        Long userId = this.userService.getUserId();
        Long id = obj.getId();
        String dateString = obj.getDate();
        Account account = null;
        try {
            account = service.findById(id, dateString).get();
        } catch (NoSuchElementException e) {
            logger.error(MessageTemplates.notFoundMessage(Account.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Account.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(account.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Account.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Account.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(account);
    }

    /**
     * Add new account
     * @param account new account
     * @return response with saved account
     */
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Account account) {
        Long userId = this.userService.getUserId();
        if (account.getId() != null && account.getId() != 0) {
            logger.error("Account id must be null");
            return new ResponseEntity<>(MessageTemplates.idMustBeNull(Account.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (account.getName() == null ||
        account.getColor() == null ||
        account.getIcon() == null ||
        account.getCreditLimit() == null ||
        account.getStartBalance() == null ||
        account.getUserId() == null) {
            logger.error(MessageTemplates.emptyFields(Account.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(Account.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(account.getUserId(), userId)) {
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Account.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(account));
    }

    /**
     * Update existing account
     * @param account account with existing id but new data
     * @return response with updated account
     */
    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestBody Account account) {
        Long userId = this.userService.getUserId();
        Long id = account.getId();
        if (account.getId() == null || account.getId() == 0) {
            logger.error(MessageTemplates.idIsNull(Account.class));
            return new ResponseEntity<>(MessageTemplates.idIsNull(Account.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (account.getName() == null ||
                account.getColor() == null ||
                account.getIcon() == null ||
                account.getCreditLimit() == null ||
                account.getStartBalance() == null ||
                account.getUserId() == null) {
            logger.error(MessageTemplates.emptyFields(Account.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(Account.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(account.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Account.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Account.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.findById(id, new SimpleDateFormat("yyyy-MM-dd").format(new Date())).isPresent()) {
            logger.error(MessageTemplates.notFoundMessage(Account.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Account.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(account));
    }

    /**
     * Delete exists account by id
     * @param id of existing account that should be deleted
     * @return response with 200 status
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (id == null || id == 0) {
            logger.error(MessageTemplates.missedIdMessage(Account.class));
            return new ResponseEntity<>(MessageTemplates.missedIdMessage(Account.class), HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error(MessageTemplates.notFoundMessage(Account.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Account.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
