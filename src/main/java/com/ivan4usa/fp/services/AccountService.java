package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entity.Account;
import com.ivan4usa.fp.repository.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private AccountRepository repository;
    @Autowired
    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public List<Account> findAll(Long userId) {
        return repository.findAccountsByUserId(userId);
    }

    public Account findById(Long id) {
        return repository.findById(id).isPresent() ? repository.findById(id).get() : null;
    }

    public Account add(Account account) {
        return repository.save(account);
    }

    public Account update(Account account) {
        return repository.save(account);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
