package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entities.Account;
import com.ivan4usa.fp.entities.Currency;
import com.ivan4usa.fp.fixer.Rates;
import com.ivan4usa.fp.fixer.RatesService;
import com.ivan4usa.fp.services.CurrencyService;
import com.ivan4usa.fp.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final CurrencyService service;
    private final UserService userService;
    private final RatesService ratesService;
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService service, UserService userService, RatesService ratesService, CurrencyService currencyService) {
        this.service = service;
        this.userService = userService;
        this.ratesService = ratesService;
        this.currencyService = currencyService;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Currency>> findAll(@RequestBody Long userId) {
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error("Currency is not match to user id of " + userId);
            return new ResponseEntity("Currency is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.findAll(userId));
    }

    @PostMapping("/id")
    public ResponseEntity<Currency> findById(@RequestBody Long id) {
        Long userId = this.userService.getUserId();
        Currency currency = null;
        try {
            currency = service.findById(id).get();
        } catch (NoSuchElementException e) {
            logger.error("Currency with id = " + id + " not found");
            return new ResponseEntity("Currency not found", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(currency.getUserId(), userId)) {
            logger.error("Currency is not match to user id of " + id);
            return new ResponseEntity("Currency is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(currency);
    }

    @PostMapping("/add")
    public ResponseEntity<Currency> add(@RequestBody Currency currency) {
        Long userId = this.userService.getUserId();
        if (currency.getId() != null && currency.getId() != 0) {
            logger.error("Currency id must be null");
            return new ResponseEntity("Currency id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (currency.getName() == null ||
                currency.getUserId() == null) {
            logger.error("Some fields of currency are empty");
            return new ResponseEntity("Some fields of currency are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(currency.getUserId(), userId)) {
            return new ResponseEntity("Currency is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(currency));
    }

    @PatchMapping("/update")
    public ResponseEntity<Currency> update(@RequestBody Currency currency) {
        Long userId = this.userService.getUserId();
        Long id = currency.getId();
        if (currency.getId() == null && currency.getId() == 0) {
            logger.error("Currency id is null");
            return new ResponseEntity("Currency id is null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (currency.getName() == null ||
                currency.getUserId() == null) {
            logger.error("Some fields of currency are empty");
            return new ResponseEntity("Some fields of currency are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(currency.getUserId(), userId)) {
            return new ResponseEntity("Currency is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.findById(id).isPresent()) {
            logger.error("Currency id" + id + " is not found");
            return new ResponseEntity("Currency id " + id + " is not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(currency));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Account> delete(@PathVariable("id") Long id) {
        if (id == null || id == 0) {
            return new ResponseEntity("Currency id missed", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Currency id "+ id + " not found");
            return new ResponseEntity("Currency id " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("rates")
    public ResponseEntity<Rates> updateRates(@RequestBody String date) throws IOException {
        return ResponseEntity.ok(ratesService.loadRatesByDateRapid(date));
    }
}
