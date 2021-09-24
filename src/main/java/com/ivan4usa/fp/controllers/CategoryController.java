package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entity.Category;
import com.ivan4usa.fp.services.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Category>> findAll(@RequestBody Long userId) {
        return ResponseEntity.ok(service.findAll(userId));
    }

    @PostMapping("/id")
    public ResponseEntity<Category> findById(@RequestBody Long id) {
        Category category = null;
        try {
            category = service.findById(id);
        } catch (NoSuchElementException e) {
            logger.error("category with id = " + id + " not found");
            return new ResponseEntity("category not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(category);
    }

    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {
        if (category.getId() != null || category.getId() != 0) {
            logger.error("account id must be null");
            return new ResponseEntity("account id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (category.getName() == null ||
                category.getIcon() == null ||
                category.getUserId() == null) {
            logger.error("Some fields of category are empty");
            return new ResponseEntity("Some fields of category are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(category));
    }
}
