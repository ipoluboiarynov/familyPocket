package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entities.Template;
import com.ivan4usa.fp.services.TemplateService;
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
@RequestMapping("/api/template")
public class TemplateController {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final TemplateService service;
    private final UserService userService;

    @Autowired
    public TemplateController(TemplateService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Template>> findAll(@RequestBody Long userId) {
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error("Template is not match to user id of " + userId);
            return new ResponseEntity("Template is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.findAll(userId));
    }

    @PostMapping("/id")
    public ResponseEntity<Template> findById(@RequestBody Long id) {
        Long userId = this.userService.getUserId();
        Template template = null;
        try {
            template = service.findById(id).get();
        } catch (NoSuchElementException e) {
            logger.error("Template with id = " + id + " not found");
            return new ResponseEntity("Template not found", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(template.getUserId(), userId)) {
            logger.error("Template is not match to user id of " + id);
            return new ResponseEntity("Template is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(template);
    }

    @PostMapping("/add")
    public ResponseEntity<Template> add(@RequestBody Template template) {
        Long userId = this.userService.getUserId();
        if (template.getId() != null && template.getId() != 0) {
            logger.error("Template id must be null");
            return new ResponseEntity("Template id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (template.getName() == null ||
                template.getUserId() == null) {
            logger.error("Some fields of template are empty");
            return new ResponseEntity("Some fields of template are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(template.getUserId(), userId)) {
            return new ResponseEntity("Template is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(template));
    }

    @PatchMapping("/update")
    public ResponseEntity<Template> update(@RequestBody Template template) {
        Long userId = this.userService.getUserId();
        Long id = template.getId();
        if (template.getId() == null && template.getId() == 0) {
            logger.error("Template id is null");
            return new ResponseEntity("Template id is null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (template.getName() == null ||
                template.getUserId() == null) {
            logger.error("Some fields of template are empty");
            return new ResponseEntity("Some fields of template are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(template.getUserId(), userId)) {
            return new ResponseEntity("Template is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.findById(id).isPresent()) {
            logger.error("Template id" + id + " is not found");
            return new ResponseEntity("Template id " + id + " is not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(template));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Template> delete(@PathVariable("id") Long id) {
        if (id == null || id == 0) {
            return new ResponseEntity("Template id missed", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Template id "+ id + " not found");
            return new ResponseEntity("Template id " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
