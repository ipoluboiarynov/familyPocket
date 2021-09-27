package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entity.Record;
import com.ivan4usa.fp.enums.RecordType;
import com.ivan4usa.fp.repository.RecordRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
class RecordServiceTest {

    @Autowired
    private RecordService service;

    @MockBean
    private RecordRepository repository;

    @Test
    void findAll() throws ParseException {
        // Set up a mock repository
        Record record1 = new Record(1L, new SimpleDateFormat("yyyy-MM-dd").parse("2020-04-02"),
                new BigDecimal("1000.00"), "comment", RecordType.INCOME, 1L, null,
                null, null);

        Record record2 = new Record(2L, new SimpleDateFormat("yyyy-MM-dd").parse("2020-04-03"),
                new BigDecimal("1000.00"), "comment", RecordType.INCOME, 1L, null,
                null, null);

        Record record3 = new Record(3L, new SimpleDateFormat("yyyy-MM-dd").parse("2020-04-04"),
                new BigDecimal("1000.00"), "comment", RecordType.INCOME, 1L, null,
                null, null);
        doReturn(Arrays.asList(record1, record2, record3)).when(repository).findRecordsByUserId(1L);
        // Execute the service call
        List<Record> records = service.findAll(1L);

        // Assert the response
        Assertions.assertEquals(3, records.size(), "findAll should return 3 accounts");
    }

    @Test
    void findById() throws ParseException {
        // Set up a mock repository
        Record record = new Record(8L, new SimpleDateFormat("yyyy-MM-dd").parse("2021-07-04"),
                new BigDecimal("2000.00"), "comment", RecordType.INCOME, 5L, null,
                null, null);
        doReturn(Optional.of(record)).when(repository).findById(8L);
        // Execute the service call
        Optional<Record> returnedRecord = service.findById(8L);
        // Assert the response
        Assertions.assertNotNull(Optional.of(returnedRecord).get(), "Record was not found");
        Assertions.assertSame(returnedRecord.get(), record, "The record returned was not the same as the mock");
    }

    @Test
    void testFindByIdNotFound() {
        // Set up a mock repository
        doReturn(Optional.empty()).when(repository).findById(1L);
        // Execute the service call
        Optional<Record> returnedRecord = service.findById(1L);
        // Assert the response
        Assertions.assertFalse(returnedRecord.isPresent(), "Record should not be found");
    }

    @Test
    void add() throws ParseException {
        // Set up a mock repository
        Record newRecord = new Record(1L, new SimpleDateFormat("yyyy-MM-dd").parse("2021-02-02"),
                new BigDecimal("100.00"), "comment", RecordType.INCOME, 5L, null,
                null, null);
        doReturn(newRecord).when(repository).save(any());
        // Execute the service call
        Record returnedRecord = service.add(newRecord);
        // Assert the response
        Assertions.assertNotNull(returnedRecord, "The saved record should not be null");
        Assertions.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2021-02-02"), returnedRecord.getRecordDate(), "The name should be different");
        Assertions.assertEquals(RecordType.INCOME, returnedRecord.getRecordType(), "The record type should be different");
        Assertions.assertEquals("comment", returnedRecord.getComment(), "The comment should be different");
        Assertions.assertEquals(new BigDecimal("100.00"), returnedRecord.getAmount(), "The amount should be different");
        Assertions.assertEquals(null, returnedRecord.getAccount(), "The account should be different");
        Assertions.assertEquals(null, returnedRecord.getCategory(), "The category should be different");
        Assertions.assertEquals(null, returnedRecord.getCurrency(), "The currency should be different");
        Assertions.assertEquals(5L, returnedRecord.getUserId(), "The user id should be different");
    }

    @Test
    void update() throws ParseException {
        // Set up a mock repository
        Record updateRecord = new Record(7L, new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01"),
                new BigDecimal("100.00"), "comment", RecordType.TRANSFER, 2L, null,
                null, null);
        doReturn(updateRecord).when(repository).save(any());
        // Execute the service call
        Record returnedRecord = service.update(updateRecord);
        // Assert the response
        Assertions.assertNotNull(returnedRecord, "The saved record should not be null");
        Assertions.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01"), returnedRecord.getRecordDate(), "The name should be different");
        Assertions.assertEquals(RecordType.TRANSFER, returnedRecord.getRecordType(), "The record type should be different");
        Assertions.assertEquals("comment", returnedRecord.getComment(), "The comment should be different");
        Assertions.assertEquals(new BigDecimal("100.00"), returnedRecord.getAmount(), "The amount should be different");
        Assertions.assertEquals(null, returnedRecord.getAccount(), "The account should be different");
        Assertions.assertEquals(null, returnedRecord.getCategory(), "The category should be different");
        Assertions.assertEquals(null, returnedRecord.getCurrency(), "The currency should be different");
        Assertions.assertEquals(2L, returnedRecord.getUserId(), "The user id should be different");
    }

    @Test
    void delete() throws ParseException {
        // Set up a mock repository
        Record deleteRecord = new Record(4L, new SimpleDateFormat("yyyy-MM-dd").parse("2021-06-08"),
                new BigDecimal("100.00"), "comment", RecordType.TRANSFER, 2L, null,
                null, null);
        doNothing().when(repository).deleteById(4L);
        // Execute the service call
        service.add(deleteRecord);
        service.delete(4L);
        Optional<Record> returnedRecord = service.findById(4L);
        // Assert the response
        Assertions.assertFalse(returnedRecord.isPresent(), "Record should not be found");
    }
}
