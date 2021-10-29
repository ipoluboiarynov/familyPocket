package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entities.Record;
import com.ivan4usa.fp.services.RecordService;
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
@RequestMapping("/api/record")
public class RecordController {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final RecordService service;
    private final UserService userService;

    @Autowired
    public RecordController(RecordService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Record>> findAll(@RequestBody Long userId) {
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error("Record is not match to user id of " + userId);
            return new ResponseEntity("Record is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.findAll(userId));
    }

    @PostMapping("/id")
    public ResponseEntity<Record> findById(@RequestBody Long id) {
        Long userId = this.userService.getUserId();
        Record record = null;
        try {
            record = service.findById(id).get();
        } catch (NoSuchElementException e) {
            logger.error("Record with id = " + id + " not found");
            return new ResponseEntity("Record not found", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(record.getUserId(), userId)) {
            logger.error("Record is not match to user id of " + id);
            return new ResponseEntity("Record is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(record);
    }

    @PostMapping("/add")
    public ResponseEntity<Record> add(@RequestBody Record record) {
        Long userId = this.userService.getUserId();
        if (record.getId() != null && record.getId() != 0) {
            logger.error("Record id must be null");
            return new ResponseEntity("Record id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (record.getRecordDate() == null ||
                record.getAmount() == null ||
                record.getRecordType() == null ||
                record.getUserId() == null) {
            logger.error("Some fields of record are empty");
            return new ResponseEntity("Some fields of record are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(record.getUserId(), userId)) {
            return new ResponseEntity("Record is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(record));
    }

    @PatchMapping("/update")
    public ResponseEntity<Record> update(@RequestBody Record record) {
        Long userId = this.userService.getUserId();
        Long id = record.getId();
        if (record.getId() == null && record.getId() == 0) {
            logger.error("Record id is null");
            return new ResponseEntity("Record id is null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (record.getRecordDate() == null ||
                record.getAmount() == null ||
                record.getRecordType() == null ||
                record.getUserId() == null) {
            logger.error("Some fields of record are empty");
            return new ResponseEntity("Some fields of record are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(record.getUserId(), userId)) {
            return new ResponseEntity("Record is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.findById(id).isPresent()) {
            logger.error("Record id" + id + " is not found");
            return new ResponseEntity("Record id " + id + " is not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(record));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Record> delete(@PathVariable("id") Long id) {
        if (id == null || id == 0) {
            return new ResponseEntity("id missed", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Record id "+ id + " not found");
            return new ResponseEntity("Record id " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
