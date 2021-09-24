package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entity.Filter;
import com.ivan4usa.fp.services.FilterService;
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
@RequestMapping("/api/filter")
public class FilterController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final FilterService service;

    @Autowired
    public FilterController(FilterService service) {
        this.service = service;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Filter>> findAll(@RequestBody Long userId) {
        return ResponseEntity.ok(service.findAll(userId));
    }

    @PostMapping("/id")
    public ResponseEntity<Filter> findById(@RequestBody Long id) {
        Filter filter = null;
        try {
            filter = service.findById(id);
        } catch (NoSuchElementException e) {
            logger.error("filter with id = " + id + " not found");
            return new ResponseEntity("filter not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(filter);
    }

    @PostMapping("/add")
    public ResponseEntity<Filter> add(@RequestBody Filter filter) {
        if (filter.getId() != null || filter.getId() != 0) {
            logger.error("filter id must be null");
            return new ResponseEntity("filter id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (filter.getName() == null ||
                filter.getUserId() == null) {
            logger.error("Some fields of filter are empty");
            return new ResponseEntity("Some fields of filter are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(filter));
    }

    @PatchMapping("/update")
    public ResponseEntity<Filter> update(@RequestBody Filter filter) {
        if (filter.getId() == null || filter.getId() == 0) {
            logger.error("filter id is null");
            return new ResponseEntity("filter id is null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (filter.getName() == null ||
                filter.getUserId() == null) {
            logger.error("Some fields of filter are empty");
            return new ResponseEntity("Some fields of filter are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(filter));
    }

    @DeleteMapping("delete")
    public ResponseEntity<Filter> delete(@RequestBody Long id) {
        if (id == null || id == 0) {
            return new ResponseEntity("id missed", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("filter id "+ id + " not found");
            return new ResponseEntity("filter id " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
