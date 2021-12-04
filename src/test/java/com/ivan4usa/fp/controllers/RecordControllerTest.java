package com.ivan4usa.fp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.ivan4usa.fp.entities.Account;
import com.ivan4usa.fp.entities.Category;
import com.ivan4usa.fp.entities.Filter;
import com.ivan4usa.fp.entities.Record;
import com.ivan4usa.fp.enums.RecordType;
import com.ivan4usa.fp.search.PageParams;
import com.ivan4usa.fp.search.RecordSearchValues;
import com.ivan4usa.fp.services.RecordService;
import com.ivan4usa.fp.services.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = RecordController.class)
class RecordControllerTest {

    @MockBean
    private RecordService service;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        // Set up a mocked service
        Record record1 = new Record(1L, LocalDate.parse("2020-04-02"),
                new BigDecimal("1000.00"), "comment", RecordType.INCOME, 1L,
                null, null);

        Record record2 = new Record(2L, LocalDate.parse("2020-04-03"),
                new BigDecimal("3000.00"), "comment", RecordType.TR_IN, 1L, null,
                null);

        when(userService.getUserId()).thenReturn(1L);
        doReturn(Lists.newArrayList(record1, record2)).when(service).findAll(1L);
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/record/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(1L)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].recordDate[0]", is(2020)))
                .andExpect(jsonPath("$[0].recordDate[1]", is(4)))
                .andExpect(jsonPath("$[0].recordDate[2]", is(2)))
                .andExpect(jsonPath("$[0].amount", is(1000.00)))
                .andExpect(jsonPath("$[0].comment", is("comment")))
                .andExpect(jsonPath("$[0].recordType", is("INCOME")))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].account", is(nullValue())))
                .andExpect(jsonPath("$[0].category", is(nullValue())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].recordDate[0]", is(2020)))
                .andExpect(jsonPath("$[1].recordDate[1]", is(4)))
                .andExpect(jsonPath("$[1].recordDate[2]", is(3)))
                .andExpect(jsonPath("$[1].amount", is(3000.00)))
                .andExpect(jsonPath("$[1].comment", is("comment")))
                .andExpect(jsonPath("$[1].recordType", is("TR_IN")))
                .andExpect(jsonPath("$[1].userId", is(1)))
                .andExpect(jsonPath("$[1].account", is(nullValue())))
                .andExpect(jsonPath("$[1].category", is(nullValue())));
    }

    @Test
    void findById() throws Exception {
        Record record = new Record(8L, LocalDate.parse("2020-04-02"),
                new BigDecimal("1000.00"), "comment", RecordType.INCOME, 1L, null,
                null);
        when(userService.getUserId()).thenReturn(1L);
        doReturn(Optional.of(record)).when(service).findById(8L);
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/record/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(8L)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(8)))
                .andExpect(jsonPath("$.recordDate[0]", is(2020)))
                .andExpect(jsonPath("$.recordDate[1]", is(4)))
                .andExpect(jsonPath("$.recordDate[2]", is(2)))
                .andExpect(jsonPath("$.amount", is(1000.00)))
                .andExpect(jsonPath("$.comment", is("comment")))
                .andExpect(jsonPath("$.recordType", is("INCOME")))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.account", is(nullValue())))
                .andExpect(jsonPath("$.category", is(nullValue())));
    }

    @Test
    void getTotalNumber() throws Exception {
        when(userService.getUserId()).thenReturn(1L);
        doReturn(2).when(service).getTotalNumber(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/record/total")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(1L)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$", is(2)));
    }

    @Test
    void search() throws Exception {
        Record record1 = new Record(4L, LocalDate.parse("2021-01-02"),
                new BigDecimal("1500.00"), "some comment", RecordType.EXPENSE, 1L, null,
                null);

        Record record2 = new Record(5L, LocalDate.parse("2021-01-03"),
                new BigDecimal("15700.00"), "some comment 2", RecordType.EXPENSE, 1L, null,
                null);

        when(userService.getUserId()).thenReturn(1L);
        Filter filter = new Filter(1L, "test", LocalDate.parse("2021-01-01"), LocalDate.parse("2021-11-01"), 1L,
                RecordType.INCOME, new ArrayList<Account>(), new ArrayList<Category>());
        PageParams params = new PageParams(100, 0);
        RecordSearchValues searchValues = new RecordSearchValues(filter, params);

        List<Record> recordList = new ArrayList<>();
        recordList.add(record1);
        recordList.add(record2);
        Page<Record> pagedResponse = new PageImpl(recordList);

        doReturn(pagedResponse).when(service).search(filter.getRecordType(), filter.getStartDate(),
                filter.getEndDate(), filter.getUserId(), null, null,
                PageRequest.of(params.getPageNumber(), params.getPageSize()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/record/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(searchValues)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(4)))
                .andExpect(jsonPath("$.content[0].recordDate[0]", is(2021)))
                .andExpect(jsonPath("$.content[0].recordDate[1]", is(1)))
                .andExpect(jsonPath("$.content[0].recordDate[2]", is(2)))
                .andExpect(jsonPath("$.content[0].amount", is(1500.00)))
                .andExpect(jsonPath("$.content[0].comment", is("some comment")))
                .andExpect(jsonPath("$.content[0].recordType", is("EXPENSE")))
                .andExpect(jsonPath("$.content[0].userId", is(1)))
                .andExpect(jsonPath("$.content[0].account", is(nullValue())))
                .andExpect(jsonPath("$.content[0].category", is(nullValue())))
                .andExpect(jsonPath("$.content[1].id", is(5)))
                .andExpect(jsonPath("$.content[1].recordDate[0]", is(2021)))
                .andExpect(jsonPath("$.content[1].recordDate[1]", is(1)))
                .andExpect(jsonPath("$.content[1].recordDate[2]", is(3)))
                .andExpect(jsonPath("$.content[1].amount", is(15700.00)))
                .andExpect(jsonPath("$.content[1].comment", is("some comment 2")))
                .andExpect(jsonPath("$.content[1].recordType", is("EXPENSE")))
                .andExpect(jsonPath("$.content[1].userId", is(1)))
                .andExpect(jsonPath("$.content[1].account", is(nullValue())))
                .andExpect(jsonPath("$.content[1].category", is(nullValue())))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.size", is(2)));
    }

    @Test
    void addTransfer() throws Exception {
        Record recordFromAccount = new Record(null, LocalDate.parse("2021-01-02"),
                new BigDecimal("1500.00"), "", RecordType.TR_IN, 1L, null,
                null);
        Record recordToAccount = new Record(null, LocalDate.parse("2021-01-03"),
                new BigDecimal("15700.00"), "", RecordType.TR_OUT, 1L, null,
                null);

        Record recordFromAccountReturned = new Record(1L, LocalDate.parse("2021-01-02"),
                new BigDecimal("1500.00"), "2", RecordType.TR_IN, 1L, null,
                null);
        Record recordToAccountReturned = new Record(2L, LocalDate.parse("2021-01-03"),
                new BigDecimal("15700.00"), "1", RecordType.TR_OUT, 1L, null,
                null);

        Record[] records = new Record[2];
        records[0] = recordFromAccount;
        records[1] = recordToAccount;

        Record[] recordsReturned = new Record[2];
        recordsReturned[0] = recordFromAccountReturned;
        recordsReturned[1] = recordToAccountReturned;

        when(userService.getUserId()).thenReturn(1L);
        doReturn(recordsReturned).when(service).addTransfer(recordFromAccount, recordToAccount);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/record/transfer/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(records)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].recordDate[0]", is(2021)))
                .andExpect(jsonPath("$[0].recordDate[1]", is(1)))
                .andExpect(jsonPath("$[0].recordDate[2]", is(2)))
                .andExpect(jsonPath("$[0].amount", is(1500.00)))
                .andExpect(jsonPath("$[0].comment", is("2")))
                .andExpect(jsonPath("$[0].recordType", is("TR_IN")))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].account", is(nullValue())))
                .andExpect(jsonPath("$[0].category", is(nullValue())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].recordDate[0]", is(2021)))
                .andExpect(jsonPath("$[1].recordDate[1]", is(1)))
                .andExpect(jsonPath("$[1].recordDate[2]", is(3)))
                .andExpect(jsonPath("$[1].amount", is(15700.00)))
                .andExpect(jsonPath("$[1].comment", is("1")))
                .andExpect(jsonPath("$[1].recordType", is("TR_OUT")))
                .andExpect(jsonPath("$[1].userId", is(1)))
                .andExpect(jsonPath("$[1].account", is(nullValue())))
                .andExpect(jsonPath("$[1].category", is(nullValue())));
    }

    @Test
    void add() throws Exception {
        // Set up a mocked service
        Record recordPost = new Record(null, LocalDate.parse("2020-04-02"),
                new BigDecimal("1000.00"), "comment", RecordType.INCOME, 1L, null,
                null);
        Record recordReturn = new Record(4L, LocalDate.parse("2020-04-02"),
                new BigDecimal("1000.00"), "comment", RecordType.INCOME, 1L, null,
                null);
        when(userService.getUserId()).thenReturn(1L);
        doReturn(recordReturn).when(service).add(any());
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/record/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(recordPost)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.recordDate[0]", is(2020)))
                .andExpect(jsonPath("$.recordDate[1]", is(4)))
                .andExpect(jsonPath("$.recordDate[2]", is(2)))
                .andExpect(jsonPath("$.amount", is(1000.00)))
                .andExpect(jsonPath("$.comment", is("comment")))
                .andExpect(jsonPath("$.recordType", is("INCOME")))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.account", is(nullValue())))
                .andExpect(jsonPath("$.category", is(nullValue())));
    }

    @Test
    void update() throws Exception {
        // Set up a mocked service
        Record recordPatch = new Record(3L, LocalDate.parse("2020-04-04"),
                new BigDecimal("4000.00"), "comment", RecordType.INCOME, 5L, null,
                null);

        Record recordFoundById = new Record(3L, LocalDate.parse("2020-04-04"),
                new BigDecimal("4000.00"), "comment", RecordType.INCOME, 5L, null,
                null);

        Record recordReturn = new Record(3L, LocalDate.parse("2020-04-04"),
                new BigDecimal("4000.00"), "comment", RecordType.INCOME, 5L, null,
                null);
        when(service.findById(3L)).thenReturn(Optional.of(recordFoundById));
        when(userService.getUserId()).thenReturn(5L);
        doReturn(recordReturn).when(service).update(any());

        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/record/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(recordPatch)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.recordDate[0]", is(2020)))
                .andExpect(jsonPath("$.recordDate[1]", is(4)))
                .andExpect(jsonPath("$.recordDate[2]", is(4)))
                .andExpect(jsonPath("$.amount", is(4000.00)))
                .andExpect(jsonPath("$.comment", is("comment")))
                .andExpect(jsonPath("$.recordType", is("INCOME")))
                .andExpect(jsonPath("$.userId", is(5)))
                .andExpect(jsonPath("$.account", is(nullValue())))
                .andExpect(jsonPath("$.category", is(nullValue())));
    }

    @Test
    void delete() throws Exception {
        // Set up a mocked service
        Record recordDelete = new Record(5L, LocalDate.parse("2020-04-02"),
                new BigDecimal("6000.00"), "comment", RecordType.INCOME, 1L, null,
                null);
        when(userService.getUserId()).thenReturn(1L);
        doNothing().when(service).delete(any());
        // Execute the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/record/delete/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(recordDelete)))
                // Validate the response code and content type
                .andExpect(status().isOk());
        // Validate the service usage
        verify(service, times(1)).delete(5L);
    }

    static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = JsonMapper.builder()
                    .addModule(new ParameterNamesModule())
                    .addModule(new Jdk8Module())
                    .addModule(new JavaTimeModule())
                    .build();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
