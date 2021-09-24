package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entity.AccountType;
import com.ivan4usa.fp.repository.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountTypeService {
    private final AccountTypeRepository repository;

    @Autowired
    public AccountTypeService(AccountTypeRepository repository) {
        this.repository = repository;
    }

    public List<AccountType> findAll(Long userId) {
        return repository.findAccountTypesByUserId(userId);
    }

    public AccountType findById(Long id) {
        return repository.findById(id).isPresent() ? repository.findById(id).get() : null;
    }

    public AccountType add(AccountType accountType) {
        return repository.save(accountType);
    }

    public AccountType update(AccountType accountType) {
        return repository.save(accountType);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
