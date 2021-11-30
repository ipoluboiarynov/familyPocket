package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.entities.Account;
import com.ivan4usa.fp.entities.Category;
import com.ivan4usa.fp.entities.Filter;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

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

    @PostMapping("/total")
    public ResponseEntity<Integer> getTotalNumber(@RequestBody Long userId) {
        Long checkUserId = this.userService.getUserId();
        if (!Objects.equals(checkUserId, userId)) {
            logger.error("Record is not match to user id of " + userId);
            return new ResponseEntity("Record is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.getTotalNumber(userId));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Record>> search(@RequestBody RecordSearchValues searchValues) {
        Long userId = this.userService.getUserId();
        RecordType recordType = searchValues.getFilter().getRecordType() != null ? searchValues.getFilter().getRecordType() : null;
        LocalDate startDate = searchValues.getFilter().getStartDate() != null ? searchValues.getFilter().getStartDate() : null;
        LocalDate endDate = searchValues.getFilter().getEndDate() != null ? searchValues.getFilter().getEndDate() : null;
        List<Long> account_ids = new ArrayList<>();
        if (searchValues.getFilter().getAccounts().size() > 0) {
            for (Account account: searchValues.getFilter().getAccounts()) {
                account_ids.add(account.getId());
            }
        } else {
            account_ids = null;
        }
        List<Long> category_ids = new ArrayList<>();
        if (searchValues.getFilter().getCategories().size() > 0) {
            for (Category category: searchValues.getFilter().getCategories()) {
                category_ids.add(category.getId());
            }
        } else {
            category_ids = null;
        }
        int pageSize = searchValues.getPageParams().getPageSize() != null ? searchValues.getPageParams().getPageSize()
                : 100;
        int pageNumber = searchValues.getPageParams().getPageNumber() != null ? searchValues.getPageParams().getPageNumber()
                : 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return ResponseEntity.ok(service.search(recordType, startDate, endDate, userId, account_ids, category_ids, pageRequest));
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

    @PostMapping("/transfer/add")
    public ResponseEntity<Record[]> addTransfer(@RequestBody ArrayList<Record> records) {
        Long userId = this.userService.getUserId();
        Record recordFromAccount = records.get(0);
        Record recordToAccount = records.get(1);

        if (recordFromAccount.getId() != null && recordFromAccount.getId() != 0 &&
                recordToAccount.getId() != null && recordToAccount.getId() != 0) {
            logger.error("Record id must be null");
            return new ResponseEntity("Record id must be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (recordFromAccount.getRecordDate() == null ||
                recordFromAccount.getAmount() == null ||
                recordFromAccount.getRecordType() == null ||
                recordFromAccount.getUserId() == null) {
            logger.error("Some fields of record from account are empty");
            return new ResponseEntity("Some fields of record from accountare empty", HttpStatus.NOT_ACCEPTABLE);
        }

        if (recordToAccount.getRecordDate() == null ||
                recordToAccount.getAmount() == null ||
                recordToAccount.getRecordType() == null ||
                recordToAccount.getUserId() == null) {
            logger.error("Some fields of record to account are empty");
            return new ResponseEntity("Some fields of record to account are empty", HttpStatus.NOT_ACCEPTABLE);
        }

        if (!Objects.equals(recordFromAccount.getUserId(), userId)) {
            return new ResponseEntity("Record from account is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (!Objects.equals(recordToAccount.getUserId(), userId)) {
            return new ResponseEntity("Record to account is not match to user id", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(service.addTransfer(recordFromAccount, recordToAccount));
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

    @DeleteMapping("/delete/{id}")
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
