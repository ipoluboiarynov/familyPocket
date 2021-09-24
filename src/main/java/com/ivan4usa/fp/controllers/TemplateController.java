package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entity.Template;
import com.ivan4usa.fp.services.TemplateService;
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
@RequestMapping("/api/template")
public class TemplateController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final TemplateService service;

    @Autowired
    public TemplateController(TemplateService service) {
        this.service = service;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Template>> findAll(@RequestBody Long userId) {
        return ResponseEntity.ok(service.findAll(userId));
    }

    @PostMapping("/id")
    public ResponseEntity<Template> findById(@RequestBody Long id) {
        Template template = null;
        try {
            template = service.findById(id);
        } catch (NoSuchElementException e) {
            logger.error("template with id = " + id + " not found");
            return new ResponseEntity("template not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(template);
    }

    @PostMapping("/add")
    public ResponseEntity<Template> add(@RequestBody Template template) {
        if (template.getId() != null || template.getId() != 0) {
            logger.error("template id must be null");
            return new ResponseEntity("template id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (template.getName() == null ||
                template.getRecordType() == null ||
                template.getUserId() == null) {
            logger.error("Some fields of template are empty");
            return new ResponseEntity("Some fields of template are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(template));
    }

    @PatchMapping("/update")
    public ResponseEntity<Template> update(@RequestBody Template template) {
        if (template.getId() == null || template.getId() == 0) {
            logger.error("template id is null");
            return new ResponseEntity("template id is null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (template.getName() == null ||
                template.getRecordType() == null ||
                template.getUserId() == null) {
            logger.error("Some fields of template are empty");
            return new ResponseEntity("Some fields of template are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(template));
    }

    @DeleteMapping("delete")
    public ResponseEntity<Template> delete(@RequestBody Long id) {
        if (id == null || id == 0) {
            return new ResponseEntity("id missed", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("template id "+ id + " not found");
            return new ResponseEntity("template id " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
