package com.ivan4usa.fp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan4usa.fp.entity.Currency;
import com.ivan4usa.fp.services.CurrencyService;
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
class CurrencyControllerTest {

    @MockBean
    private CurrencyService service;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        // Set up a mocked service
        Currency currency1 = new Currency(1L, "EUR", "icon1", false, 4L);
        Currency currency2 = new Currency(2L, "GBP", "icon2", false, 4L);
        when(userService.getUserId()).thenReturn(4L);
        doReturn(Lists.newArrayList(currency1, currency2)).when(service).findAll(4L);
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/currency/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(4L)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("EUR")))
                .andExpect(jsonPath("$[0].icon", is("icon1")))
                .andExpect(jsonPath("$[0].base", is(false)))
                .andExpect(jsonPath("$[0].userId", is(4)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("GBP")))
                .andExpect(jsonPath("$[1].icon", is("icon2")))
                .andExpect(jsonPath("$[1].base", is(false)))
                .andExpect(jsonPath("$[1].userId", is(4)));
    }

    @Test
    void findById() throws Exception {
        // Set up a mocked service
        Currency currency = new Currency(10L, "JPY", "icon", true, 2L);
        when(userService.getUserId()).thenReturn(2L);
        doReturn(Optional.of(currency)).when(service).findById(10L);
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/currency/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(10L)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.name", is("JPY")))
                .andExpect(jsonPath("$.icon", is("icon")))
                .andExpect(jsonPath("$.base", is(true)))
                .andExpect(jsonPath("$.userId", is(2)));
    }

    @Test
    void add() throws Exception {
        // Set up a mocked service
        Currency currencyPost = new Currency(null, "EUR", "icon", false, 4L);
        Currency currencyReturn = new Currency(2L, "EUR", "icon", false, 4L);

        when(userService.getUserId()).thenReturn(4L);
        doReturn(currencyReturn).when(service).add(any());
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/currency/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(currencyPost)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("EUR")))
                .andExpect(jsonPath("$.icon", is("icon")))
                .andExpect(jsonPath("$.base", is(false)))
                .andExpect(jsonPath("$.userId", is(4)));
    }

    @Test
    void update() throws Exception {
        // Set up a mocked service
        Currency currencyPatch = new Currency(7L, "EUR", "icon", false, 1L);
        Currency currencyFoundById = new Currency(7L, "EUR", "icon", false, 1L);
        Currency currencyReturn = new Currency(7L, "EUR", "icon", false, 1L);
        when(service.findById(7L)).thenReturn(Optional.of(currencyFoundById));
        when(userService.getUserId()).thenReturn(1L);
        doReturn(currencyReturn).when(service).update(any());

        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/currency/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(currencyPatch)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(7)))
                .andExpect(jsonPath("$.name", is("EUR")))
                .andExpect(jsonPath("$.icon", is("icon")))
                .andExpect(jsonPath("$.base", is(false)))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    void delete() throws Exception {
        Currency currencyDelete = new Currency(7L, "JPY", "icon", true, 5L);
        when(userService.getUserId()).thenReturn(5L);
        doNothing().when(service).delete(any());
        // Execute the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/currency/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(currencyDelete)))
                // Validate the response code and content type
                .andExpect(status().isOk());
        // Validate the service usage
        verify(service, times(1)).delete(7L);

    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
