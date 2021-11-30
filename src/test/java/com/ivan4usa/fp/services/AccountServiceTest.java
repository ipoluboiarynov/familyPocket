package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.Account;
import com.ivan4usa.fp.repositories.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
@ContextConfiguration(classes = AccountService.class)
class AccountServiceTest {

    @Autowired
    private AccountService service;

    @MockBean
    private AccountRepository repository;

    @Test
    void findAll() {
        // Set up a mock repository
        Account account1 = new Account();
        account1.setId(1L);
        account1.setName("Account 1");
        account1.setIcon("icon 1");
        account1.setColor("color 1");
        account1.setCreditLimit(new BigDecimal("1000.00"));
        account1.setStartBalance(new BigDecimal("1.00"));
        account1.setUserId(2L);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setName("Account 2");
        account2.setIcon("icon 2");
        account2.setColor("color 2");
        account2.setCreditLimit(new BigDecimal("2000.00"));
        account2.setStartBalance(new BigDecimal("2.00"));
        account2.setUserId(2L);

        Account account3 = new Account();
        account3.setId(3L);
        account3.setName("Account 3");
        account3.setIcon("icon 3");
        account3.setColor("color 3");
        account3.setCreditLimit(new BigDecimal("3000.00"));
        account3.setStartBalance(new BigDecimal("3.00"));
        account3.setUserId(2L);

        doReturn(Arrays.asList(account1, account2, account3)).when(repository).findAccountsByUserId(2L);
        // Execute the service call
        List<Account> accounts = service.findAll(2L, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        // Assert the response
        Assertions.assertEquals(3, accounts.size(), "findAll should return 3 accounts");
    }

    @Test
    void findById() {
        // Set up a mock repository
        Account account = new Account();
        account.setId(10L);
        account.setName("Account");
        account.setIcon("icon");
        account.setColor("color");
        account.setCreditLimit(new BigDecimal("1000.00"));
        account.setStartDate(LocalDate.now());
        account.setStartBalance(new BigDecimal("1.00"));
        account.setUserId(1L);
        BigDecimal balance = new BigDecimal("1000.00");
        account.setBalance(balance);
        doReturn(Optional.of(account)).when(repository).findById(10L);
        doReturn(balance).when(repository).getBalanceByAccountId(10L, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        doReturn(Optional.of(account)).when(repository).findAccountById(10L);
        // Execute the service call
        Optional<Account> returnedAccount = service.findById(10L, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        // Assert the response
        Assertions.assertNotNull(Optional.of(returnedAccount).get(), "Account was not found");
        Assertions.assertSame(returnedAccount.get(), account, "The account returned was not the same as the mock");
    }

    @Test
    void testFindByIdNotFound() {
        // Set up a mock repository
        doReturn(Optional.empty()).when(repository).findById(1L);
        // Execute the service call
        Optional<Account> returnedAccount = service.findById(1L, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        // Assert the response
        Assertions.assertFalse(returnedAccount.isPresent(), "Account should not be found");
    }

    @Test
    void add() {
        // Set up a mock repository
        Account account = new Account();
        account.setId(5L);
        account.setName("Account");
        account.setIcon("icon");
        account.setColor("color");
        account.setCreditLimit(new BigDecimal("1000.00"));
        account.setStartBalance(new BigDecimal("1.00"));
        account.setUserId(1L);

        doReturn(account).when(repository).save(any());
        // Execute the service call
        Account returnedAccount = service.add(account);
        // Assert the response
        Assertions.assertNotNull(returnedAccount, "The saved account should not be null");
        Assertions.assertEquals("Account", returnedAccount.getName(), "The name should be different");
        Assertions.assertEquals("icon", returnedAccount.getIcon(), "The icon should be different");
        Assertions.assertEquals("color", returnedAccount.getColor(), "The color should be different");
        Assertions.assertEquals(new BigDecimal("1000.00"), returnedAccount.getCreditLimit(), "The credit limit should be different");
        Assertions.assertEquals(new BigDecimal("1.00"), returnedAccount.getStartBalance(), "The start balance should be different");
        Assertions.assertEquals(1L, returnedAccount.getUserId(), "The user id should be different");
    }

    @Test
    void update() {
        // Set up a mock repository
        Account account = new Account();
        account.setId(15L);
        account.setName("Account 1");
        account.setIcon("icon 1");
        account.setColor("color 1");
        account.setCreditLimit(new BigDecimal("5000.00"));
        account.setStartBalance(new BigDecimal("100.00"));
        account.setUserId(2L);
        doReturn(account).when(repository).save(any());
        // Execute the service call
        Account returnedAccount = service.update(account);
        // Assert the response
        Assertions.assertNotNull(returnedAccount, "The updated account should not be null");
        Assertions.assertEquals("Account 1", returnedAccount.getName(), "The name should be different");
        Assertions.assertEquals("icon 1", returnedAccount.getIcon(), "The icon should be different");
        Assertions.assertEquals("color 1", returnedAccount.getColor(), "The color should be different");
        Assertions.assertEquals(new BigDecimal("5000.00"), returnedAccount.getCreditLimit(), "The credit limit should be different");
        Assertions.assertEquals(new BigDecimal("100.00"), returnedAccount.getStartBalance(), "The start balance should be different");
        Assertions.assertEquals(2L, returnedAccount.getUserId(), "The user id should be different");
    }

    @Test
    void delete() {
        // Set up a mock repository
        Account account = new Account();
        account.setId(8L);
        account.setName("Account 8");
        account.setIcon("icon 8");
        account.setColor("color 8");
        account.setCreditLimit(new BigDecimal("2000.00"));
        account.setStartBalance(new BigDecimal("150.00"));
        account.setUserId(3L);
        doNothing().when(repository).deleteById(8L);
        // Execute the service call
        service.add(account);
        service.delete(8L);
        Optional<Account> returnedAccount = service.findById(8L, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        // Assert the response
        Assertions.assertFalse(returnedAccount.isPresent(), "Account should not be found");
    }
}
