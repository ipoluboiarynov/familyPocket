package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entity.Filter;
import com.ivan4usa.fp.services.FilterService;
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
@RequestMapping("/api/filter")
public class FilterController {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final FilterService service;
    private final UserService userService;

    @Autowired
    public FilterController(FilterService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Filter>> findAll(@RequestBody Long userId) {
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error("Filter is not match to user id of " + userId);
            return new ResponseEntity("Filter is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.findAll(userId));
    }

    @PostMapping("/id")
    public ResponseEntity<Filter> findById(@RequestBody Long id) {
        Long userId = this.userService.getUserId();
        Filter filter = null;
        try {
            filter = service.findById(id).get();
        } catch (NoSuchElementException e) {
            logger.error("Filter with id = " + id + " not found");
            return new ResponseEntity("Filter not found", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(filter.getUserId(), userId)) {
            logger.error("Filter is not match to user id of " + id);
            return new ResponseEntity("Filter is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(filter);
    }

    @PostMapping("/add")
    public ResponseEntity<Filter> add(@RequestBody Filter filter) {
        Long userId = this.userService.getUserId();
        if (filter.getId() != null && filter.getId() != 0) {
            logger.error("filter id must be null");
            return new ResponseEntity("filter id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (filter.getName() == null ||
                filter.getUserId() == null) {
            logger.error("Some fields of filter are empty");
            return new ResponseEntity("Some fields of filter are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(filter.getUserId(), userId)) {
            return new ResponseEntity("Filter is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(filter));
    }

    @PatchMapping("/update")
    public ResponseEntity<Filter> update(@RequestBody Filter filter) {
        Long userId = this.userService.getUserId();
        Long id = filter.getId();
        if (filter.getId() == null && filter.getId() == 0) {
            logger.error("Filter id is null");
            return new ResponseEntity("Filter id is null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (filter.getName() == null ||
                filter.getUserId() == null) {
            logger.error("Some fields of filter are empty");
            return new ResponseEntity("Some fields of filter are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(filter.getUserId(), userId)) {
            return new ResponseEntity("Filter is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.findById(id).isPresent()) {
            logger.error("Filter id" + id + " is not found");
            return new ResponseEntity("Filter id " + id + " is not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(filter));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Filter> delete(@PathVariable("id") Long id) {
        if (id == null || id == 0) {
            return new ResponseEntity("Filter id missed", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Filter id "+ id + " not found");
            return new ResponseEntity("filter id " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
