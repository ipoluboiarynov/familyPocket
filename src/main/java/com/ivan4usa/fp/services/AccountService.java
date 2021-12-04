package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.Account;
import com.ivan4usa.fp.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * The service that is called from the corresponding controller and controls actions on the Account
 */
@Service
public class AccountService {

    private AccountRepository repository;

    @Autowired
    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public List<Account> findAll(Long userId, String date) {
        List<Account> list = repository.findAccountsByUserId(userId);
        list.forEach(account -> {
            Long id = account.getId();
            BigDecimal balance = this.repository.getBalanceByAccountId(id, date);
            account.setBalance(balance);
        });
        return list;
    }

    public Optional<Account> findById(Long id, String date) {
        if (this.repository.findAccountById(id).isPresent()) {
            Account account = this.repository.findAccountById(id).get();
            BigDecimal balance = this.repository.getBalanceByAccountId(id, date);
            account.setBalance(balance);
            return Optional.of(account);
        }
        return Optional.empty();
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
