package com.ivan4usa.fp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan4usa.fp.entity.Filter;
import com.ivan4usa.fp.enums.RecordType;
import com.ivan4usa.fp.services.FilterService;
import com.ivan4usa.fp.services.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureMockMvc(addFilters = false)
class FilterControllerTest {

    @MockBean
    private FilterService service;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        // Set up a mocked service
        Filter filter1 = new Filter(1L, "Filter 1",
                new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-12"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-12"),
                2L, RecordType.EXPENSE, new ArrayList<>(), new ArrayList<>());
        Filter filter2 = new Filter(2L, "Filter 2",
                new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-12"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-12"),
                2L, RecordType.INCOME, new ArrayList<>(), new ArrayList<>());
        when(userService.getUserId()).thenReturn(2L);
        doReturn(Lists.newArrayList(filter1, filter2)).when(service).findAll(2L);
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/filter/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(2L)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Filter 1")))
                .andExpect(jsonPath("$[0].startDate", is("2020-05-12T05:00:00.000+00:00")))
                .andExpect(jsonPath("$[0].endDate", is("2020-06-12T05:00:00.000+00:00")))
                .andExpect(jsonPath("$[0].userId", is(2)))
                .andExpect(jsonPath("$[0].recordType", is("EXPENSE")))
                .andExpect(jsonPath("$[0].accounts", is(new ArrayList<>())))
                .andExpect(jsonPath("$[0].categories", is(new ArrayList<>())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Filter 2")))
                .andExpect(jsonPath("$[1].startDate", is("2020-05-12T05:00:00.000+00:00")))
                .andExpect(jsonPath("$[1].endDate", is("2020-06-12T05:00:00.000+00:00")))
                .andExpect(jsonPath("$[1].userId", is(2)))
                .andExpect(jsonPath("$[1].recordType", is("INCOME")))
                .andExpect(jsonPath("$[1].accounts", is(new ArrayList<>())))
                .andExpect(jsonPath("$[1].categories", is(new ArrayList<>())));
    }

    @Test
    void findById() throws Exception {
        // Set up a mocked service
        Filter filter = new Filter(1L, "Filter",
                new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-05"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2021-09-12"),
                2L, RecordType.INCOME, new ArrayList<>(), new ArrayList<>());
        when(userService.getUserId()).thenReturn(2L);
        doReturn(Optional.of(filter)).when(service).findById(1L);
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/filter/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(1L)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Filter")))
                .andExpect(jsonPath("$.startDate", is("2021-08-05T05:00:00.000+00:00")))
                .andExpect(jsonPath("$.endDate", is("2021-09-12T05:00:00.000+00:00")))
                .andExpect(jsonPath("$.userId", is(2)))
                .andExpect(jsonPath("$.recordType", is("INCOME")))
                .andExpect(jsonPath("$.accounts", is(new ArrayList<>())))
                .andExpect(jsonPath("$.categories", is(new ArrayList<>())));
    }

    @Test
    void add() throws Exception {
        Filter filterPost = new Filter(null, "Filter",
                new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-05"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2021-09-12"),
                5L, RecordType.INCOME, new ArrayList<>(), new ArrayList<>());
        Filter filterReturn = new Filter(1L, "Filter",
                new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-05"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2021-09-12"),
                5L, RecordType.INCOME, new ArrayList<>(), new ArrayList<>());

        when(userService.getUserId()).thenReturn(5L);
        doReturn(filterReturn).when(service).add(any());
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/filter/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(filterPost)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Filter")))
                .andExpect(jsonPath("$.startDate", is("2021-08-05T05:00:00.000+00:00")))
                .andExpect(jsonPath("$.endDate", is("2021-09-12T05:00:00.000+00:00")))
                .andExpect(jsonPath("$.userId", is(5)))
                .andExpect(jsonPath("$.recordType", is("INCOME")))
                .andExpect(jsonPath("$.accounts", is(new ArrayList<>())))
                .andExpect(jsonPath("$.categories", is(new ArrayList<>())));
    }

    @Test
    void update() throws Exception {
        // Set up a mocked service
        Filter filterPatch = new Filter(4L, "Filter Update",
                new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-05"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2021-09-12"),
                5L, RecordType.INCOME, new ArrayList<>(), new ArrayList<>());

        Filter filterFoundById = new Filter(4L, "Filter Update",
                new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-05"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2021-09-12"),
                5L, RecordType.INCOME, new ArrayList<>(), new ArrayList<>());

        Filter filterReturn = new Filter(4L, "Filter Update",
                new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-05"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2021-09-12"),
                5L, RecordType.INCOME, new ArrayList<>(), new ArrayList<>());

        when(service.findById(4L)).thenReturn(Optional.of(filterFoundById));
        when(userService.getUserId()).thenReturn(5L);
        doReturn(filterReturn).when(service).update(any());

        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/filter/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(filterPatch)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.name", is("Filter Update")))
                .andExpect(jsonPath("$.startDate", is("2021-08-05T05:00:00.000+00:00")))
                .andExpect(jsonPath("$.endDate", is("2021-09-12T05:00:00.000+00:00")))
                .andExpect(jsonPath("$.userId", is(5)))
                .andExpect(jsonPath("$.recordType", is("INCOME")))
                .andExpect(jsonPath("$.accounts", is(new ArrayList<>())))
                .andExpect(jsonPath("$.categories", is(new ArrayList<>())));
    }

    @Test
    void delete() throws Exception {
        // Set up a mocked service
        Filter filterDelete = new Filter(4L, "Filter Delete",
                new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-08"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2021-09-25"),
                5L, RecordType.TRANSFER, new ArrayList<>(), new ArrayList<>());

        when(userService.getUserId()).thenReturn(5L);
        doNothing().when(service).delete(any());
        // Execute the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/filter/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(filterDelete)))
                // Validate the response code and content type
                .andExpect(status().isOk());
        // Validate the service usage
        verify(service, times(1)).delete(4L);
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}