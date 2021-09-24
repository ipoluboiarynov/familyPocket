package com.ivan4usa.fp.controllers;

import com.ivan4usa.fp.services.AccountTypeService;
import com.ivan4usa.fp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

//@TestPropertySource("classpath:application-local.properties")
class AccountTypeControllerTest {

    AccountTypeController accountTypeController;
    UserService userService;
    AccountTypeService service;

    @Test
    void findById() {

    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAll() {
    }

    @Test
    void testFindById() {
    }

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}
