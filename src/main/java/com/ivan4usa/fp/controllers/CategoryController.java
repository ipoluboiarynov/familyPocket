package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entity.Category;
import com.ivan4usa.fp.services.CategoryService;
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
@RequestMapping("/api/category")
public class CategoryController {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final CategoryService service;
    private final UserService userService;

    @Autowired
    public CategoryController(CategoryService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Category>> findAll(@RequestBody Long userId) {
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error("Category is not match to user id of " + userId);
            return new ResponseEntity("Category is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.findAll(userId));
    }

    @PostMapping("/id")
    public ResponseEntity<Category> findById(@RequestBody Long id) {
        Long userId = this.userService.getUserId();
        Category category = null;
        try {
            category = service.findById(id).get();
        } catch (NoSuchElementException e) {
            logger.error("Category with id = " + id + " not found");
            return new ResponseEntity("Category not found", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(category.getUserId(), userId)) {
            logger.error("Category is not match to user id of " + id);
            return new ResponseEntity("Category is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(category);
    }

    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {
        Long userId = this.userService.getUserId();
        if (category.getId() != null && category.getId() != 0) {
            logger.error("Category id must be null");
            return new ResponseEntity("Category id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (category.getName() == null ||
                category.getIcon() == null ||
                category.getUserId() == null) {
            logger.error("Some fields of category are empty");
            return new ResponseEntity("Some fields of category are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(category.getUserId(), userId)) {
            return new ResponseEntity("Category is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(category));
    }

    @PatchMapping("/update")
    public ResponseEntity<Category> update(@RequestBody Category category) {
        Long userId = this.userService.getUserId();
        Long id = category.getId();
        if (category.getId() == null || category.getId() == 0) {
            logger.error("Category id is null");
            return new ResponseEntity("Category id is null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (category.getName() == null || category.getUserId() == null) {
            logger.error("Some fields of category are empty");
            return new ResponseEntity("Some fields of category are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(category.getUserId(), userId)) {
            return new ResponseEntity("Category is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.findById(id).isPresent()) {
            logger.error("Category id" + id + " is not found");
            return new ResponseEntity("Category id " + id + " is not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(category));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Category> delete(@PathVariable("id") Long id) {
        if (id == null || id == 0) {
            return new ResponseEntity("Category id missed", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Category id "+ id + " not found");
            return new ResponseEntity("Category id " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
