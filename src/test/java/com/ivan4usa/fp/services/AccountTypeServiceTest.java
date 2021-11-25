package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.AccountType;
import com.ivan4usa.fp.repositories.AccountTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
@ContextConfiguration(classes = AccountTypeService.class)
class AccountTypeServiceTest {

    @Autowired
    private AccountTypeService service;

    @MockBean
    private AccountTypeRepository repository;

    @Test
    void findAll() {
        // Set up a mock repository
        AccountType accountType1 = new AccountType(1L, "Account Type 1", true, 1L);
        AccountType accountType2 = new AccountType(2L, "Account Type 2", false, 1L);
        AccountType accountType3 = new AccountType(3L, "Account Type 3", true, 1L);
        doReturn(Arrays.asList(accountType1, accountType2, accountType3)).when(repository).findAccountTypesByUserId(1L);
        // Execute the service call
        List<AccountType> accountTypes = service.findAll(1L);
        // Assert the response
        Assertions.assertEquals(3, accountTypes.size(), "findAll should return 3 accountTypes");
    }

    @Test
    void findById() {
        // Set up a mock repository
        AccountType accountType = new AccountType(1L, "Account Type", true, 1L);
        doReturn(Optional.of(accountType)).when(repository).findById(1L);
        // Execute the service call
        Optional<AccountType> returnedAccountType = service.findById(1L);
        // Assert the response
        Assertions.assertNotNull(Optional.of(returnedAccountType).get(), "The account type was not found");
        Assertions.assertSame(returnedAccountType.get(), accountType, "The account type returned was not the same as the mock");
    }

    @Test
    void testFindByIdNotFound() {
        // Set up a mock repository
        doReturn(Optional.empty()).when(repository).findById(1L);
        // Execute the service call
        Optional<AccountType> returnedAccountType = service.findById(1L);
        // Assert the response
        Assertions.assertFalse(returnedAccountType.isPresent(), "Account Type should not be found");
    }

    @Test
    void add() {
        // Set up a mock repository
        AccountType accountType = new AccountType(1L, "Account Type", true, 1L);
        doReturn(accountType).when(repository).save(any());
        // Execute the service call
        AccountType returnedAccountType = service.add(accountType);
        // Assert the response
        Assertions.assertNotNull(returnedAccountType, "The saved account type should not be null");
        Assertions.assertEquals("Account Type", returnedAccountType.getName(), "The name should be different");
        Assertions.assertEquals(true, returnedAccountType.isNegative(), "The negative balance property value should not be different");
        Assertions.assertEquals(1L, returnedAccountType.getUserId(), "The user id should not be different");
    }

    @Test
    void update() {
        // Set up a mock repository
        AccountType accountType = new AccountType(1L, "Account Type", true, 1L);
        doReturn(accountType).when(repository).save(any());
        // Execute the service call
        AccountType returnedAccountType = service.update(accountType);
        // Assert the response
        Assertions.assertNotNull(returnedAccountType, "The updated account type should not be null");
        Assertions.assertEquals("Account Type", returnedAccountType.getName(), "The name should be different");
        Assertions.assertEquals(true, returnedAccountType.isNegative(), "The negative balance property value should not be different");
        Assertions.assertEquals(1L, returnedAccountType.getUserId(), "The name should not be different");
    }

    @Test
    void delete() {
        // Set up a mock repository
        AccountType accountType = new AccountType(1L, "Account Type", true, 1L);
        doNothing().when(repository).deleteById(1L);
        // Execute the service call
        service.add(accountType);
        service.delete(1L);
        Optional<AccountType> returnedAccountType = service.findById(1L);
        // Assert the response
        Assertions.assertFalse(returnedAccountType.isPresent(), "Account Type should not be found");
    }
}
