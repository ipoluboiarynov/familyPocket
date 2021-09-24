package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entity.Account;
import com.ivan4usa.fp.entity.Currency;
import com.ivan4usa.fp.services.CurrencyService;
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
@RequestMapping("/api/currency")
public class CurrencyController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final CurrencyService service;

    @Autowired
    public CurrencyController(CurrencyService service) {
        this.service = service;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Currency>> findAll(@RequestBody Long userId) {
        return ResponseEntity.ok(service.findAll(userId));
    }

    @PostMapping("/id")
    public ResponseEntity<Currency> findById(@RequestBody Long id) {
        Currency currency = null;
        try {
            currency = service.findById(id);
        } catch (NoSuchElementException e) {
            logger.error("currency with id = " + id + " not found");
            return new ResponseEntity("currency not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(currency);
    }

    @PostMapping("/add")
    public ResponseEntity<Currency> add(@RequestBody Currency currency) {
        if (currency.getId() != null || currency.getId() != 0) {
            logger.error("currency id must be null");
            return new ResponseEntity("currency id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (currency.getName() == null ||
                currency.getUserId() == null) {
            logger.error("Some fields of currency are empty");
            return new ResponseEntity("Some fields of currency are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(currency));
    }

    @PatchMapping("/update")
    public ResponseEntity<Currency> update(@RequestBody Currency currency) {
        if (currency.getId() == null || currency.getId() == 0) {
            logger.error("currency id is null");
            return new ResponseEntity("currency id is null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (currency.getName() == null ||
                currency.getUserId() == null) {
            logger.error("Some fields of currency are empty");
            return new ResponseEntity("Some fields of currency are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(currency));
    }

    @DeleteMapping("delete")
    public ResponseEntity<Account> delete(@RequestBody Long id) {
        if (id == null || id == 0) {
            return new ResponseEntity("id missed", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("currency id "+ id + " not found");
            return new ResponseEntity("currency id " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
