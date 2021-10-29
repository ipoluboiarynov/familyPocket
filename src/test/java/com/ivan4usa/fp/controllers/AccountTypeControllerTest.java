package com.ivan4usa.fp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan4usa.fp.entities.AccountType;
import com.ivan4usa.fp.services.AccountTypeService;
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

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureMockMvc(addFilters = false)
class AccountTypeControllerTest {

    @MockBean
    private AccountTypeService service;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        // Set up a mocked service
        AccountType accountType1 = new AccountType(1L, "Account Type 1", true, 12L);
        AccountType accountType2 = new AccountType(2L, "Account Type 2", false, 12L);
        when(userService.getUserId()).thenReturn(12L);
        doReturn(Lists.newArrayList(accountType1, accountType2)).when(service).findAll(12L);
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account-type/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(12L)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Account Type 1")))
                .andExpect(jsonPath("$[0].negative", is(true)))
                .andExpect(jsonPath("$[0].userId", is(12)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Account Type 2")))
                .andExpect(jsonPath("$[1].negative", is(false)))
                .andExpect(jsonPath("$[1].userId", is(12)));
    }

    @Test
    void findById() throws Exception {
        // Set up a mocked service
        AccountType accountType = new AccountType(15L, "Account Type 15", true, 1L);
        when(userService.getUserId()).thenReturn(1L);
        doReturn(Optional.of(accountType)).when(service).findById(15L);
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account-type/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(15L)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(15)))
                .andExpect(jsonPath("$.name", is("Account Type 15")))
                .andExpect(jsonPath("$.negative", is(true)))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    void add() throws Exception {
        // Set up a mocked service
        AccountType accountTypePost = new AccountType(null, "Account Type", true, 1L);
        AccountType accountTypeReturn = new AccountType(1L, "Account Type", true, 1L);
        when(userService.getUserId()).thenReturn(1L);
        doReturn(accountTypeReturn).when(service).add(any());
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account-type/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(accountTypePost)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Account Type")))
                .andExpect(jsonPath("$.negative", is(true)))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    void update() throws Exception {
        // Set up a mocked service
        AccountType accountTypePatch = new AccountType(10L, "Account Type", true, 5L);
        AccountType accountTypeFoundById = new AccountType(10L, "Account Type", true, 5L);
        AccountType accountTypeReturn = new AccountType(10L, "Account Type", true, 5L);
        when(service.findById(10L)).thenReturn(Optional.of(accountTypeFoundById));
        when(userService.getUserId()).thenReturn(5L);
        doReturn(accountTypeReturn).when(service).update(any());

        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/account-type/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(accountTypePatch)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.name", is("Account Type")))
                .andExpect(jsonPath("$.negative", is(true)))
                .andExpect(jsonPath("$.userId", is(5)));
    }

    @Test
    void delete() throws Exception {
        // Set up a mocked service
        AccountType accountTypeDelete = new AccountType(1L, "Account Type", true, 1L);
        when(userService.getUserId()).thenReturn(1L);
        doNothing().when(service).delete(any());
        // Execute the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/account-type/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(accountTypeDelete)))
                // Validate the response code and content type
                .andExpect(status().isOk());
        // Validate the service usage
        verify(service, times(1)).delete(1L);
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
