package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.Filter;
import com.ivan4usa.fp.enums.RecordType;
import com.ivan4usa.fp.repositories.FilterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
@ContextConfiguration(classes = FilterService.class)
class FilterServiceTest {

    @Autowired
    private FilterService service;

    @MockBean
    private FilterRepository repository;

    @Test
    void findAll() throws ParseException {
        // Set up a mock repository
        Filter filter1 = new Filter(1L, "Filter 1", LocalDate.parse("2021-02-14"),
                LocalDate.parse("2021-03-14"), 2L, RecordType.EXPENSE, new ArrayList<>(), new ArrayList<>());
        Filter filter2 = new Filter(2L, "Filter 2", LocalDate.parse("2021-02-14"),
                LocalDate.parse("2021-03-14"), 2L, RecordType.INCOME, new ArrayList<>(), new ArrayList<>());
        Filter filter3 = new Filter(3L, "Filter 3", LocalDate.parse("2021-02-14"),
                LocalDate.parse("2021-03-14"), 2L, RecordType.EXPENSE, new ArrayList<>(), new ArrayList<>());
        doReturn(Arrays.asList(filter1, filter2, filter3)).when(repository).findFiltersByUserId(2L);
        // Execute the service call
        List<Filter> filters = service.findAll(2L);
        // Assert the response
        Assertions.assertEquals(3, filters.size(), "findAll should return 3 filters");
    }

    @Test
    void findById() {
        Filter filter = new Filter(15L, "Filter", null, null, 3L, RecordType.EXPENSE, new ArrayList<>(), new ArrayList<>());
        doReturn(Optional.of(filter)).when(repository).findById(15L);
        // Execute the service call
        Optional<Filter> returnedFilter = service.findById(15L);
        // Assert the response
        Assertions.assertNotNull(Optional.of(returnedFilter).get(), "Filter was not found");
        Assertions.assertSame(returnedFilter.get(), filter, "The filter returned was not the same as the mock");
    }

    @Test
    void testFindByIdNotFound() {
        // Set up a mock repository
        doReturn(Optional.empty()).when(repository).findById(1L);
        // Execute the service call
        Optional<Filter> returnedFilter = service.findById(1L);
        // Assert the response
        Assertions.assertFalse(returnedFilter.isPresent(), "Filter should not be found");
    }

    @Test
    void add() {
        Filter filter = new Filter(7L, "Filter", null, null, 2L, RecordType.EXPENSE, new ArrayList<>(), new ArrayList<>());
        doReturn(filter).when(repository).save(any());
        // Execute the service call
        Filter returnedFilter = service.add(filter);
        // Assert the response
        Assertions.assertNotNull(returnedFilter, "The saved filter should not be null");
        Assertions.assertEquals("Filter", returnedFilter.getName(), "The name should be different");
        Assertions.assertEquals(null, returnedFilter.getStartDate(), "The starting date should be different");
        Assertions.assertEquals(null, returnedFilter.getEndDate(), "The end date should be different");
        Assertions.assertEquals(2L, returnedFilter.getUserId(), "The user id should be different");
        Assertions.assertEquals(RecordType.EXPENSE, returnedFilter.getRecordType(), "The record type should be different");
        Assertions.assertEquals(new ArrayList<>(), returnedFilter.getAccounts(), "The accounts should be different");
        Assertions.assertEquals(new ArrayList<>(), returnedFilter.getCategories(), "The categories should be different");
    }

    @Test
    void update() {
        // Set up a mock repository
        Filter filter = new Filter(2L, "Filter Test", null, null, 2L, RecordType.TR_IN, new ArrayList<>(), new ArrayList<>());
        doReturn(filter).when(repository).save(any());
        // Execute the service call
        Filter returnedFilter = service.update(filter);
        // Assert the response
        Assertions.assertNotNull(returnedFilter, "The saved filter should not be null");
        Assertions.assertEquals("Filter Test", returnedFilter.getName(), "The name should be different");
        Assertions.assertEquals(null, returnedFilter.getStartDate(), "The starting date should be different");
        Assertions.assertEquals(null, returnedFilter.getEndDate(), "The end date should be different");
        Assertions.assertEquals(2L, returnedFilter.getUserId(), "The user id should be different");
        Assertions.assertEquals(RecordType.TR_IN, returnedFilter.getRecordType(), "The record type should be different");
        Assertions.assertEquals(new ArrayList<>(), returnedFilter.getAccounts(), "The accounts should be different");
        Assertions.assertEquals(new ArrayList<>(), returnedFilter.getCategories(), "The categories should be different");
    }

    @Test
    void delete() {
        // Set up a mock repository
        Filter filter = new Filter(4L, "Filter Delete", null, null, 5L, RecordType.TR_IN, new ArrayList<>(), new ArrayList<>());
        doNothing().when(repository).deleteById(4L);
        // Execute the service call
        service.add(filter);
        service.delete(4L);
        Optional<Filter> returnedFilter = service.findById(4L);
        // Assert the response
        Assertions.assertFalse(returnedFilter.isPresent(), "Filter should not be found");
    }
}
