package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.constants.MessageTemplates;
import com.ivan4usa.fp.entities.Account;
import com.ivan4usa.fp.entities.Category;
import com.ivan4usa.fp.entities.Record;
import com.ivan4usa.fp.enums.RecordType;
import com.ivan4usa.fp.search.RecordSearchValues;
import com.ivan4usa.fp.services.RecordService;
import com.ivan4usa.fp.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The controller that receives requests for operations on Record
 */
@Controller
@EnableWebMvc
@CrossOrigin
@RestController
@RequestMapping("/api/record")
public class RecordController {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final RecordService service;
    private final UserService userService;

    /**
     * Constructor for class
     * @param service of RecordService
     * @param userService of UserService
     */
    @Autowired
    public RecordController(RecordService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    /**
     * Get all records by user id
     * @param userId id of user
     * @return response with list of all records for user
     */
    @PostMapping("/all")
    public ResponseEntity<?> findAll(@RequestBody Long userId) {
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error(MessageTemplates.notMatchMessage(Record.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Record.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.findAll(userId));
    }

    /**
     * Get total number of records by user id
     * @param userId id of user
     * @return response with total number of records
     */
    @PostMapping("/total")
    public ResponseEntity<?> getTotalNumber(@RequestBody Long userId) {
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error(MessageTemplates.notMatchMessage(Record.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Record.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.getTotalNumber(userId));
    }

    /**
     * Get all records that matches to search criteria
     * @param searchValues search values with filter and pagination parameters
     * @return response with found records
     */
    @PostMapping("/search")
    public ResponseEntity<Page<Record>> search(@RequestBody RecordSearchValues searchValues) {
        Long userId = this.userService.getUserId();
        RecordType recordType = searchValues.getFilter().getRecordType() != null ? searchValues.getFilter().getRecordType() : null;
        LocalDate startDate = searchValues.getFilter().getStartDate() != null ? searchValues.getFilter().getStartDate() : null;
        LocalDate endDate = searchValues.getFilter().getEndDate() != null ? searchValues.getFilter().getEndDate() : null;
        List<Long> accountIds = new ArrayList<>();
        if (searchValues.getFilter().getAccounts().size() > 0) {
            for (Account account: searchValues.getFilter().getAccounts()) {
                accountIds.add(account.getId());
            }
        } else {
            accountIds = null;
        }
        List<Long> categoryIds = new ArrayList<>();
        if (searchValues.getFilter().getCategories().size() > 0) {
            for (Category category: searchValues.getFilter().getCategories()) {
                categoryIds.add(category.getId());
            }
        } else {
            categoryIds = null;
        }
        int pageSize = searchValues.getPageParams().getPageSize() != null ? searchValues.getPageParams().getPageSize()
                : 100;
        int pageNumber = searchValues.getPageParams().getPageNumber() != null ? searchValues.getPageParams().getPageNumber()
                : 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return ResponseEntity.ok(service.search(recordType, startDate, endDate, userId, accountIds, categoryIds, pageRequest));
    }

    /**
     * Get record by id
     * @param id of record
     * @return response with found record or with appropriate error
     */
    @PostMapping("/id")
    public ResponseEntity<?> findById(@RequestBody Long id) {
        Long userId = this.userService.getUserId();
        Record record;
        try {
            record = service.findById(id).get();
        } catch (NoSuchElementException e) {
            logger.error(MessageTemplates.notFoundMessage(Record.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Record.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(record.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Record.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Record.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(record);
    }

    /**
     * Add new record
     * @param record to be added
     * @return response with added record
     */
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Record record) {
        Long userId = this.userService.getUserId();
        if (record.getId() != null && record.getId() != 0) {
            logger.error(MessageTemplates.idMustBeNull(Record.class));
            return new ResponseEntity<>(MessageTemplates.idMustBeNull(Record.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (record.getRecordDate() == null ||
                record.getAmount() == null ||
                record.getRecordType() == null ||
                record.getUserId() == null) {
            logger.error(MessageTemplates.emptyFields(Record.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(Record.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(record.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Record.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Record.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(record));
    }

    /**
     * Add record as a transfer between accounts (without category and with id of related record in thr comment)
     * @param records two related records about transfer from one account to another account
     * @return response with updated record
     */
    @PostMapping("/transfer/add")
    public ResponseEntity<?> addTransfer(@RequestBody List<Record> records) {
        Long userId = this.userService.getUserId();
        Record recordFromAccount = records.get(0);
        Record recordToAccount = records.get(1);

        if (recordFromAccount.getId() != null && recordFromAccount.getId() != 0 &&
                recordToAccount.getId() != null && recordToAccount.getId() != 0) {
            logger.error(MessageTemplates.idMustBeNull(Record.class));
            return new ResponseEntity<>(MessageTemplates.idMustBeNull(Record.class), HttpStatus.NOT_ACCEPTABLE);
        }

        if (recordFromAccount.getRecordDate() == null ||
                recordFromAccount.getAmount() == null ||
                recordFromAccount.getRecordType() == null ||
                recordFromAccount.getUserId() == null) {
            logger.error(MessageTemplates.emptyFields(Record.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(Record.class), HttpStatus.NOT_ACCEPTABLE);
        }

        if (recordToAccount.getRecordDate() == null ||
                recordToAccount.getAmount() == null ||
                recordToAccount.getRecordType() == null ||
                recordToAccount.getUserId() == null) {
            logger.error(MessageTemplates.emptyFields(Record.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(Record.class), HttpStatus.NOT_ACCEPTABLE);
        }

        if (!Objects.equals(recordFromAccount.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Record.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Record.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }

        if (!Objects.equals(recordToAccount.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Record.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Record.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(service.addTransfer(recordFromAccount, recordToAccount));
    }

    /**
     * Update existing record
     * @param record that is going to be updated
     * @return response with updated record
     */
    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestBody Record record) {
        Long userId = this.userService.getUserId();
        Long id = record.getId();
        if (record.getId() == null || record.getId() == 0) {
            logger.error(MessageTemplates.idIsNull(Record.class));
            return new ResponseEntity<>(MessageTemplates.idIsNull(Record.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (record.getRecordDate() == null ||
                record.getAmount() == null ||
                record.getRecordType() == null ||
                record.getUserId() == null) {
            logger.error(MessageTemplates.emptyFields(Record.class));
            return new ResponseEntity<>(MessageTemplates.emptyFields(Record.class), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!Objects.equals(record.getUserId(), userId)) {
            logger.error(MessageTemplates.notMatchMessage(Record.class, userId));
            return new ResponseEntity<>(MessageTemplates.notMatchMessage(Record.class, userId), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!service.findById(id).isPresent()) {
            logger.error(MessageTemplates.notFoundMessage(Record.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Record.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.update(record));
    }

    /**
     * Delete record by id
     * @param id of record
     * @return response with 200 status
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (id == null || id == 0) {
            logger.error(MessageTemplates.missedIdMessage(Record.class));
            return new ResponseEntity<>(MessageTemplates.missedIdMessage(Record.class), HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            service.delete(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error(MessageTemplates.notFoundMessage(Record.class, id));
            return new ResponseEntity<>(MessageTemplates.notFoundMessage(Record.class, id), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
