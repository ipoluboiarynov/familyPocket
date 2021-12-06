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

    /**
     * Repository instance
     */
    private final AccountRepository repository;

    /**
     * Constructor for the class
     * @param repository instance
     */
    @Autowired
    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    /**
     * Find All Accounts by user id method than found threw repository method all accounts and also adds to each account
     * the value of balance threw another repository method
     * @param userId user id
     * @param date date for limit of calculating the balance
     * @return list of accounts
     */
    public List<Account> findAll(Long userId, String date) {
        List<Account> list = repository.findAccountsByUserId(userId);
        list.forEach(account -> {
            Long id = account.getId();
            BigDecimal balance = this.repository.getBalanceByAccountId(id, date);
            account.setBalance(balance);
        });
        return list;
    }

    /**
     * Find account by id method that also adds the balance for account on the date that is passed as a input parameter
     * @param id of account
     * @param date until which will be calculated the balance
     * @return Optional object with found account or empty
     */
    public Optional<Account> findById(Long id, String date) {
        if (this.repository.findAccountById(id).isPresent()) {
            Account account = this.repository.findAccountById(id).get();
            BigDecimal balance = this.repository.getBalanceByAccountId(id, date);
            account.setBalance(balance);
            return Optional.of(account);
        }
        return Optional.empty();
    }

    /**
     * Add new account method that returns saved account
     * @param account to be saved
     * @return saved account
     */
    public Account add(Account account) {
        return repository.save(account);
    }

    /**
     * Update current account method
     * @param account account with new data
     * @return updated account
     */
    public Account update(Account account) {
        return repository.save(account);
    }

    /**
     * Delete account by id method
     * @param id of deleting account
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
