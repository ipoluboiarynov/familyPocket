package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.constants.MessageTemplates;
import com.ivan4usa.fp.entities.Template;
import com.ivan4usa.fp.services.TemplateService;
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
 * The controller that receives requests for operations on Template
 */
@Controller
@EnableWebMvc
@CrossOrigin
@RestController
@RequestMapping("/api/template")
public class TemplateController {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final TemplateService service;
    private final UserService userService;

    /**
     * Constructor for class
     * @param service of TemplateService
     * @param userService of UserService
     */
    @Autowired
    public TemplateController(TemplateService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    /**
     * Get all templates by user id
     * @param userId id of user
     * @return response with list of all templates
     */
    @PostMapping("/all")
    public ResponseEntity<?> findAll(@RequestBody Long userId) {
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error(MessageTemplates.notMatchMessage(Template.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Template.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.findAll(userId));
    }

    /**
     * Get template by id
     * @param id of template
     * @return response with found template or with "Template not found" message or with error message
     */
    @PostMapping("/id")
    public ResponseEntity<?> findById(@RequestBody Long id) {
        Long userId = this.userService.getUserId();
        Template template;
        try {
            template = service.findById(id).get();
        } catch (NoSuchElementException e) {
            logger.error(MessageTemplates.notFoundMessage(Template.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Template.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(template.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Template.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Template.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(template);
    }

    /**
     * Add new template
     * @param template to be added
     * @return response with added template
     */
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Template template) {
        Long userId = this.userService.getUserId();
        if (template.getId() != null && template.getId() != 0) {
            logger.error(MessageTemplates.idMustBeNull(Template.class));
            return new ResponseEntity<>(MessageTemplates.idMustBeNull(Template.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (template.getName() == null ||
                template.getUserId() == null) {
            logger.error(MessageTemplates.emptyFields(Template.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(Template.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(template.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Template.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Template.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(template));
    }

    /**
     * Update existing template
     * @param template that is going to be updated
     * @return response with updated template
     */
    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestBody Template template) {
        Long userId = this.userService.getUserId();
        Long id = template.getId();
        if (template.getId() == null || template.getId() == 0) {
            logger.error(MessageTemplates.idIsNull(Template.class));
            return new ResponseEntity<>(MessageTemplates.idIsNull(Template.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (template.getName() == null ||
                template.getUserId() == null) {
            logger.error(MessageTemplates.emptyFields(Template.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(Template.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(template.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Template.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Template.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.findById(id).isPresent()) {
            logger.error(MessageTemplates.notFoundMessage(Template.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Template.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(template));
    }

    /**
     * Delete template by id
     * @param id of deleting template
     * @return response with 200 status
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (id == null || id == 0) {
            logger.error(MessageTemplates.missedIdMessage(Template.class));
            return new ResponseEntity<>(MessageTemplates.missedIdMessage(Template.class), HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error(MessageTemplates.notFoundMessage(Template.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Template.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
