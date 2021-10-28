package com.ivan4usa.fp.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan4usa.fp.entity.Template;
import com.ivan4usa.fp.enums.RecordType;
import com.ivan4usa.fp.services.TemplateService;
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

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureMockMvc(addFilters = false)
class TemplateControllerTest {

    @MockBean
    private TemplateService service;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        // Set up a mock repository
        Template template1 = new Template(1L, "Template 1", new BigDecimal("1000.00"), 5L, RecordType.EXPENSE,
                null, null);

        Template template2 = new Template(2L, "Template 2", new BigDecimal("2000.00"), 5L, RecordType.TRANSFER,
                null, null);

        when(userService.getUserId()).thenReturn(5L);
        doReturn(Lists.newArrayList(template1, template2)).when(service).findAll(5L);
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/template/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(5L)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Template 1")))
                .andExpect(jsonPath("$[0].amount", is(1000.00)))
                .andExpect(jsonPath("$[0].userId", is(5)))
                .andExpect(jsonPath("$[0].recordType", is("EXPENSE")))
                .andExpect(jsonPath("$[0].category", is(nullValue())))
                .andExpect(jsonPath("$[0].account", is(nullValue())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Template 2")))
                .andExpect(jsonPath("$[1].amount", is(2000.00)))
                .andExpect(jsonPath("$[1].userId", is(5)))
                .andExpect(jsonPath("$[1].recordType", is("TRANSFER")))
                .andExpect(jsonPath("$[1].category", is(nullValue())))
                .andExpect(jsonPath("$[1].account", is(nullValue())));
    }

    @Test
    void findById() throws Exception {
        // Set up a mock repository
        Template template = new Template(1L, "Template", new BigDecimal("1000.00"), 5L, RecordType.EXPENSE,
                null, null);
        when(userService.getUserId()).thenReturn(5L);
        doReturn(Optional.of(template)).when(service).findById(1L);
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/template/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(1L)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Template")))
                .andExpect(jsonPath("$.amount", is(1000.00)))
                .andExpect(jsonPath("$.userId", is(5)))
                .andExpect(jsonPath("$.recordType", is("EXPENSE")))
                .andExpect(jsonPath("$.category", is(nullValue())))
                .andExpect(jsonPath("$.account", is(nullValue())));
    }

    @Test
    void add() throws Exception {
        // Set up a mock repository
        Template templatePost = new Template(null, "Template", new BigDecimal("1000.00"), 5L, RecordType.EXPENSE,
                null, null);
        Template templateReturn = new Template(1L, "Template", new BigDecimal("1000.00"), 5L, RecordType.EXPENSE,
                null, null);

        when(userService.getUserId()).thenReturn(5L);
        doReturn(templateReturn).when(service).add(any());
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/template/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(templatePost)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Template")))
                .andExpect(jsonPath("$.amount", is(1000.00)))
                .andExpect(jsonPath("$.userId", is(5)))
                .andExpect(jsonPath("$.recordType", is("EXPENSE")))
                .andExpect(jsonPath("$.category", is(nullValue())))
                .andExpect(jsonPath("$.account", is(nullValue())));
    }

    @Test
    void update() throws Exception {
        // Set up a mock repository
        Template templatePatch = new Template(5L, "Template", new BigDecimal("10.00"), 3L, RecordType.EXPENSE,
                null, null);
        Template templateFoundById = new Template(5L, "Template", new BigDecimal("10.00"), 3L, RecordType.EXPENSE,
                null, null);
        Template templateReturn = new Template(5L, "Template", new BigDecimal("10.00"), 3L, RecordType.EXPENSE,
                null, null);

        when(service.findById(5L)).thenReturn(Optional.of(templateFoundById));
        when(userService.getUserId()).thenReturn(3L);
        doReturn(templateReturn).when(service).update(any());

        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/template/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(templatePatch)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.name", is("Template")))
                .andExpect(jsonPath("$.amount", is(10.00)))
                .andExpect(jsonPath("$.userId", is(3)))
                .andExpect(jsonPath("$.recordType", is("EXPENSE")))
                .andExpect(jsonPath("$.category", is(nullValue())))
                .andExpect(jsonPath("$.account", is(nullValue())));
    }

    @Test
    void delete() throws Exception {
        // Set up a mock repository
        Template templateDelete = new Template(4L, "Template", new BigDecimal("10.00"), 3L, RecordType.EXPENSE,
                null, null);
        when(userService.getUserId()).thenReturn(3L);
        doNothing().when(service).delete(any());
        // Execute the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/template/delete/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(templateDelete)))
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
