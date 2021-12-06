package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.constants.MessageTemplates;
import com.ivan4usa.fp.entities.Filter;
import com.ivan4usa.fp.services.FilterService;
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
 * The controller that receives requests for operations on Filter
 */
@Controller
@EnableWebMvc
@CrossOrigin
@RestController
@RequestMapping("/api/filter")
public class FilterController {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final FilterService service;
    private final UserService userService;

    /**
     * Constructor for class
     * @param service of FilterService
     * @param userService of UserService
     */
    @Autowired
    public FilterController(FilterService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    /**
     * Get all filters by user id
     * @param userId id of user
     * @return response with found filters for user
     */
    @PostMapping("/all")
    public ResponseEntity<?> findAll(@RequestBody Long userId) {
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error(MessageTemplates.notFoundMessage(Filter.class, userId));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Filter.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.findAll(userId));
    }

    /**
     * Get filter by id
     * @param id of filter
     * @return response with found filter
     */
    @PostMapping("/id")
    public ResponseEntity<?> findById(@RequestBody Long id) {
        Long userId = this.userService.getUserId();
        Filter filter = null;
        try {
            filter = service.findById(id).get();
        } catch (NoSuchElementException e) {
            logger.error(MessageTemplates.notFoundMessage(Filter.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Filter.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(filter.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Filter.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Filter.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(filter);
    }

    /**
     * Add new filter
     * @param filter to be added
     * @return response with added filter
     */
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Filter filter) {
        Long userId = this.userService.getUserId();
        if (filter.getId() != null && filter.getId() != 0) {
            logger.error(MessageTemplates.idMustBeNull(Filter.class));
            return new ResponseEntity<>(MessageTemplates.idMustBeNull(Filter.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (filter.getName() == null ||
                filter.getUserId() == null) {
            logger.error(MessageTemplates.emptyFields(Filter.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(Filter.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(filter.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Filter.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Filter.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(filter));
    }

    /**
     * Update filter with existing id
     * @param filter with existing id and with data that should be updated
     * @return response with updated filter
     */
    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestBody Filter filter) {
        Long userId = this.userService.getUserId();
        Long id = filter.getId();
        if (filter.getId() == null || filter.getId() == 0) {
            logger.error(MessageTemplates.idIsNull(Filter.class));
            return new ResponseEntity<>(MessageTemplates.idIsNull(Filter.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (filter.getName() == null ||
                filter.getUserId() == null) {
            logger.error(MessageTemplates.emptyFields(Filter.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(Filter.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(filter.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Filter.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Filter.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.findById(id).isPresent()) {
            logger.error(MessageTemplates.notFoundMessage(Filter.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Filter.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(filter));
    }

    /**
     * Delete filter by id
     * @param id of filter
     * @return response with 200 status
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (id == null || id == 0) {
            logger.error(MessageTemplates.missedIdMessage(Filter.class));
            return new ResponseEntity<>(MessageTemplates.missedIdMessage(Filter.class), HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error(MessageTemplates.notFoundMessage(Filter.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Filter.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
