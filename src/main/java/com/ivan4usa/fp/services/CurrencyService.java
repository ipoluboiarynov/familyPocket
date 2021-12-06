package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.Currency;
import com.ivan4usa.fp.repositories.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The service that is called from the corresponding controller and controls actions on the Currency
 */
@Service
public class CurrencyService {

    /**
     * Repository instance
     */
    private CurrencyRepository repository;

    /**
     * Constructor for the class
     * @param repository instance
     */
    @Autowired
    public CurrencyService(CurrencyRepository repository) {
        this.repository = repository;
    }

    /**
     * Find all currencies by user id method
     * @param userId user id
     * @return all found currencies by user id
     */
    public List<Currency> findAll(Long userId) {
        return repository.findCurrenciesByUserId(userId);
    }

    /**
     * Find currency by id method
     * @param id of currency
     * @return Optional object with found currency or empty
     */
    public Optional<Currency> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Add new currency method
     * @param currency to be added
     * @return added currency
     */
    public Currency add(Currency currency) {
        return repository.save(currency);
    }

    /**
     * Update currency method
     * @param currency with new data
     * @return updated currency
     */
    public Currency update(Currency currency) {
        return repository.save(currency);
    }

    /**
     * Delete currency by id method
     * @param id of deleting currency
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
