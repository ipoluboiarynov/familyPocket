package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entity.Record;
import com.ivan4usa.fp.services.RecordService;
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
@RequestMapping("/api/record")
public class RecordController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final RecordService service;

    @Autowired
    public RecordController(RecordService service) {
        this.service = service;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Record>> findAll(@RequestBody Long userId) {
        return ResponseEntity.ok(service.findAll(userId));
    }

    @PostMapping("/id")
    public ResponseEntity<Record> findById(@RequestBody Long id) {
        Record record = null;
        try {
            record = service.findById(id);
        } catch (NoSuchElementException e) {
            logger.error("record with id = " + id + " not found");
            return new ResponseEntity("record not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(record);
    }

    @PostMapping("/add")
    public ResponseEntity<Record> add(@RequestBody Record record) {
        if (record.getId() != null || record.getId() != 0) {
            logger.error("record id must be null");
            return new ResponseEntity("record id must be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (record.getRecordDate() == null ||
                record.getAmount() == null ||
                record.getRecordType() == null ||
                record.getUserId() == null) {
            logger.error("Some fields of record are empty");
            return new ResponseEntity("Some fields of record are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(record));
    }

    @PatchMapping("/update")
    public ResponseEntity<Record> update(@RequestBody Record record) {
        if (record.getId() == null || record.getId() == 0) {
            logger.error("record id is null");
            return new ResponseEntity("record id is null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (record.getRecordDate() == null ||
                record.getAmount() == null ||
                record.getRecordType() == null ||
                record.getUserId() == null) {
            logger.error("Some fields of record are empty");
            return new ResponseEntity("Some fields of record are empty", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(record));
    }

    @DeleteMapping("delete")
    public ResponseEntity<Record> delete(@RequestBody Long id) {
        if (id == null || id == 0) {
            return new ResponseEntity("id missed", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("record id "+ id + " not found");
            return new ResponseEntity("record id " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
