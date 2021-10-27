package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entity.Currency;
import com.ivan4usa.fp.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {

    private CurrencyRepository repository;
    @Autowired
    public CurrencyService(CurrencyRepository repository) {
        this.repository = repository;
    }

    public List<Currency> findAll(Long userId) {
        return repository.findCurrenciesByUserId(userId);
    }

    public Optional<Currency> findById(Long id) {
        return repository.findById(id);
    }

    public Currency add(Currency currency) {
        return repository.save(currency);
    }

    public Currency update(Currency currency) {
        return repository.save(currency);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
