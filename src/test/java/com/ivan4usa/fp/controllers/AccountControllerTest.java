package com.ivan4usa.fp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan4usa.fp.entities.Account;
import com.ivan4usa.fp.repositories.AccountRepository;
import com.ivan4usa.fp.services.AccountService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @MockBean
    private AccountService service;

    @MockBean
    private AccountRepository repository;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        // Set up a mocked service
        Account account1 = new Account();
        account1.setId(1L);
        account1.setName("Account 1");
        account1.setIcon("icon");
        account1.setColor("color");
        account1.setCreditLimit(new BigDecimal("1000.00"));
        account1.setStartBalance(new BigDecimal("0.00"));
        account1.setUserId(5L);
        account1.setBalance(new BigDecimal("1000.00"));

        Account account2 = new Account();
        account2.setId(2L);
        account2.setName("Account 2");
        account2.setIcon("icon2");
        account2.setColor("color2");
        account2.setCreditLimit(new BigDecimal("2000.00"));
        account2.setStartBalance(new BigDecimal("10.00"));
        account2.setUserId(5L);
        account1.setBalance(new BigDecimal("2000.00"));

        when(userService.getUserId()).thenReturn(5L);
        doReturn(Optional.of(account1)).when(repository).findAccountById(1L);
        doReturn(Optional.of(account2)).when(repository).findAccountById(2L);
        doReturn(Lists.newArrayList(account1, account2)).when(service).findAll(5L, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));


        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(5L)))
                // Validate the response code and content type
                .andExpect(status().is4xxClientError());
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                // Validate the returned fields
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id", is(1)))
//                .andExpect(jsonPath("$[0].name", is("Account 1")))
//                .andExpect(jsonPath("$[0].icon", is("icon")))
//                .andExpect(jsonPath("$[0].color", is("color")))
//                .andExpect(jsonPath("$[0].creditLimit", is(1000.00)))
//                .andExpect(jsonPath("$[0].startBalance", is(0.00)))
//                .andExpect(jsonPath("$[0].userId", is(5)))
//                .andExpect(jsonPath("$[1].id", is(2)))
//                .andExpect(jsonPath("$[1].name", is("Account 2")))
//                .andExpect(jsonPath("$[1].icon", is("icon2")))
//                .andExpect(jsonPath("$[1].color", is("color2")))
//                .andExpect(jsonPath("$[1].creditLimit", is(2000.00)))
//                .andExpect(jsonPath("$[1].startBalance", is(10.00)))
//                .andExpect(jsonPath("$[1].userId", is(5)));
    }

    @Test
    void findById() throws Exception {
        // Set up a mocked service
        Account account = new Account();
        account.setId(1L);
        account.setName("Account 1");
        account.setIcon("icon");
        account.setColor("color");
        account.setCreditLimit(new BigDecimal("1000.00"));
        account.setStartBalance(new BigDecimal("0.00"));
        account.setUserId(7L);

        when(userService.getUserId()).thenReturn(7L);
        doReturn(Optional.of(account)).when(service).findById(7L, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(7L)))
                // Validate the response code and content type
                .andExpect(status().is4xxClientError());
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                // Validate the returned fields
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.name", is("Account 1")))
//                .andExpect(jsonPath("$.icon", is("icon")))
//                .andExpect(jsonPath("$.color", is("color")))
//                .andExpect(jsonPath("$.creditLimit", is(1000.00)))
//                .andExpect(jsonPath("$.startBalance", is(0.00)))
//                .andExpect(jsonPath("$.userId", is(7)));
    }

    @Test
    void add() throws Exception {
        // Set up a mocked service
        Account accountPost = new Account();
        accountPost.setId(null);
        accountPost.setName("Account Post");
        accountPost.setIcon("icon post");
        accountPost.setColor("color post");
        accountPost.setCreditLimit(new BigDecimal("1000.00"));
        accountPost.setStartBalance(new BigDecimal("0.00"));
        accountPost.setUserId(5L);

        Account accountReturn = new Account();
        accountReturn.setId(1L);
        accountReturn.setName("Account Post");
        accountReturn.setIcon("icon post");
        accountReturn.setColor("color post");
        accountReturn.setCreditLimit(new BigDecimal("1000.00"));
        accountReturn.setStartBalance(new BigDecimal("0.00"));
        accountReturn.setUserId(5L);

        when(userService.getUserId()).thenReturn(5L);
        doReturn(accountReturn).when(service).add(any());
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/account/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(accountPost)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Account Post")))
                .andExpect(jsonPath("$.icon", is("icon post")))
                .andExpect(jsonPath("$.color", is("color post")))
                .andExpect(jsonPath("$.creditLimit", is(1000.00)))
                .andExpect(jsonPath("$.startBalance", is(0.00)))
                .andExpect(jsonPath("$.userId", is(5)));
    }

    @Test
    void update() throws Exception {
        // Set up a mocked service
        Account accountPatch = new Account();
        accountPatch.setId(1L);
        accountPatch.setName("Account Patch");
        accountPatch.setIcon("icon patch");
        accountPatch.setColor("color patch");
        accountPatch.setCreditLimit(new BigDecimal("1000.00"));
        accountPatch.setStartBalance(new BigDecimal("0.00"));
        accountPatch.setUserId(2L);

        Account accountFoundById = new Account();
        accountFoundById.setId(1L);
        accountFoundById.setName("Account Patch");
        accountFoundById.setIcon("icon patch");
        accountFoundById.setColor("color patch");
        accountFoundById.setCreditLimit(new BigDecimal("1000.00"));
        accountFoundById.setStartBalance(new BigDecimal("0.00"));
        accountFoundById.setUserId(2L);

        Account accountReturn = new Account();
        accountReturn.setId(1L);
        accountReturn.setName("Account Patch");
        accountReturn.setIcon("icon patch");
        accountReturn.setColor("color patch");
        accountReturn.setCreditLimit(new BigDecimal("1000.00"));
        accountReturn.setStartBalance(new BigDecimal("0.00"));
        accountReturn.setUserId(2L);

        when(service.findById(1L, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))).thenReturn(Optional.of(accountFoundById));
        when(userService.getUserId()).thenReturn(2L);
        doReturn(accountReturn).when(service).update(any());

        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/account/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(accountPatch)))
                // Validate the response code and content type
                .andExpect(status().isOk());
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                // Validate the returned fields
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.name", is("Account Patch")))
//                .andExpect(jsonPath("$.icon", is("icon patch")))
//                .andExpect(jsonPath("$.color", is("color patch")))
//                .andExpect(jsonPath("$.creditLimit", is(1000.00)))
//                .andExpect(jsonPath("$.startBalance", is(0.00)))
//                .andExpect(jsonPath("$.userId", is(2)));
    }

    @Test
    void delete() throws Exception {
        // Set up a mocked service
        Account accountDelete = new Account();
        accountDelete.setId(14L);
        accountDelete.setName("Account Delete");
        accountDelete.setIcon("icon delete");
        accountDelete.setColor("color delete");
        accountDelete.setCreditLimit(new BigDecimal("5000.00"));
        accountDelete.setStartBalance(new BigDecimal("15.00"));
        accountDelete.setUserId(20L);

        when(userService.getUserId()).thenReturn(20L);
        doNothing().when(service).delete(any());
        // Execute the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/account/delete/14")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(accountDelete)))
                // Validate the response code and content type
                .andExpect(status().isOk());
        // Validate the service usage
        verify(service, times(1)).delete(14L);
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
