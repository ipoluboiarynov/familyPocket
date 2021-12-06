package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.constants.MessageTemplates;
import com.ivan4usa.fp.entities.Category;
import com.ivan4usa.fp.services.CategoryService;
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
 * The controller that receives requests for category on Category
 */
@Controller
@EnableWebMvc
@CrossOrigin
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final CategoryService service;
    private final UserService userService;

    /**
     * Constructor for class
     * @param service of CategoryService
     * @param userService of UserService
     */
    @Autowired
    public CategoryController(CategoryService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    /**
     * Get all categories by user id
     * @param userId id of user
     * @return response with list of categories for user
     */
    @PostMapping("/all")
    public ResponseEntity<?> findAll(@RequestBody Long userId) {
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error(MessageTemplates.notMatchMessage(Category.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Category.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.findAll(userId));
    }

    /**
     * Get category by id
     * @param id of Category
     * @return response with category
     */
    @PostMapping("/id")
    public ResponseEntity<?> findById(@RequestBody Long id) {
        Long userId = this.userService.getUserId();
        Category category = null;
        try {
            category = service.findById(id).get();
        } catch (NoSuchElementException e) {
            logger.error(MessageTemplates.notFoundMessage(Category.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Category.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(category.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Category.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Category.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(category);
    }

    /**
     * Add new category
     * @param category to be added
     * @return response with added category
     */
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Category category) {
        Long userId = this.userService.getUserId();
        if (category.getId() != null && category.getId() != 0) {
            logger.error(MessageTemplates.idMustBeNull(Category.class));
            return new ResponseEntity<>(MessageTemplates.idMustBeNull(Category.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (category.getName() == null ||
                category.getIcon() == null ||
                category.getUserId() == null) {
            logger.error(MessageTemplates.emptyFields(Category.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(Category.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(category.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Category.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Category.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(category));
    }

    /**
     * Update existing category
     * @param category with existing id but new data
     * @return response with updated category
     */
    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestBody Category category) {
        Long userId = this.userService.getUserId();
        Long id = category.getId();
        if (category.getId() == null || category.getId() == 0) {
            logger.error(MessageTemplates.idIsNull(Category.class));
            return new ResponseEntity<>(MessageTemplates.idIsNull(Category.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (category.getName() == null || category.getUserId() == null) {
            logger.error(MessageTemplates.emptyFields(Category.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(Category.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(category.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Category.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Category.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.findById(id).isPresent()) {
            logger.error(MessageTemplates.notFoundMessage(Category.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Category.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(category));
    }

    /**
     * Delete existing category by id
     * @param id of category
     * @return response with 200 status
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (id == null || id == 0) {
            logger.error(MessageTemplates.missedIdMessage(Category.class));
            return new ResponseEntity<>(MessageTemplates.missedIdMessage(Category.class), HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error(MessageTemplates.notFoundMessage(Category.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Category.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
