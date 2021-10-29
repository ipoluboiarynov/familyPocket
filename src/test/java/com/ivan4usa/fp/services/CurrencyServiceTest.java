package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.Currency;
import com.ivan4usa.fp.repositories.CurrencyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
class CurrencyServiceTest {

    @Autowired
    private CurrencyService service;

    @MockBean
    private CurrencyRepository repository;

    @Test
    void findAll() {
        // Set up a mock repository
        Currency currency1 = new Currency(1L, "EUR", "icon", false, 2L);
        Currency currency2 = new Currency(2L, "JPY", "icon", true, 2L);
        Currency currency3 = new Currency(3L, "GBP", "icon", false, 2L);

        doReturn(Arrays.asList(currency1, currency2, currency3)).when(repository).findCurrenciesByUserId(2L);
        // Execute the service call
        List<Currency> currencies = service.findAll(2L);

        // Assert the response
        Assertions.assertEquals(3, currencies.size(), "findAll should return 3 accounts");
    }

    @Test
    void findById() {
        // Set up a mock repository
        Currency currency = new Currency(1L, "EUR", "icon", false, 2L);
        doReturn(Optional.of(currency)).when(repository).findById(1L);
        // Execute the service call
        Optional<Currency> returnedAccount = service.findById(1L);
        // Assert the response
        Assertions.assertNotNull(Optional.of(returnedAccount).get(), "Currency was not found");
        Assertions.assertSame(returnedAccount.get(), currency, "The Currency returned was not the same as the mock");
    }

    @Test
    void testFindByIdNotFound() {
        // Set up a mock repository
        doReturn(Optional.empty()).when(repository).findById(1L);
        // Execute the service call
        Optional<Currency> returnedCurrency = service.findById(1L);
        // Assert the response
        Assertions.assertFalse(returnedCurrency.isPresent(), "Currency should not be found");
    }

    @Test
    void add() {
        // Set up a mock repository
        Currency newCurrency = new Currency(5L, "JPY", "icon", true, 4L);
        doReturn(newCurrency).when(repository).save(any());
        // Execute the service call
        Currency returnedCurrency = service.add(newCurrency);
        // Assert the response
        Assertions.assertNotNull(returnedCurrency, "The saved currency should not be null");
        Assertions.assertEquals("JPY", returnedCurrency.getName(), "The name should be different");
        Assertions.assertEquals(true, returnedCurrency.isBase(), "The base should be different");
        Assertions.assertEquals("icon", returnedCurrency.getIcon(), "The icon should be different");
        Assertions.assertEquals(4L, returnedCurrency.getUserId(), "The currency id should be different");
    }

    @Test
    void update() {
        // Set up a mock repository
        Currency updateCurrency = new Currency(1L, "EUR", "icon", false, 2L);
        doReturn(updateCurrency).when(repository).save(any());
        // Execute the service call
        Currency returnedCurrency = service.update(updateCurrency);
        // Assert the response
        Assertions.assertNotNull(returnedCurrency, "The updated currency should not be null");
        Assertions.assertEquals("EUR", returnedCurrency.getName(), "The name should be different");
        Assertions.assertEquals(false, returnedCurrency.isBase(), "The base should be different");
        Assertions.assertEquals("icon", returnedCurrency.getIcon(), "The icon should be different");
        Assertions.assertEquals(2L, returnedCurrency.getUserId(), "The currency id should be different");
    }

    @Test
    void delete() {
        // Set up a mock repository
        Currency deleteCurrency = new Currency(2L, "GBP", "icon", false, 4L);
        doNothing().when(repository).deleteById(2L);
        // Execute the service call
        service.add(deleteCurrency);
        service.delete(2L);
        Optional<Currency> returnedCurrency = service.findById(2L);
        // Assert the response
        Assertions.assertFalse(returnedCurrency.isPresent(), "Currency should not be found");
    }
}
