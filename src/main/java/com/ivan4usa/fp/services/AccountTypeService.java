package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.AccountType;
import com.ivan4usa.fp.repositories.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The service that is called from the corresponding controller and controls actions on the AccountType
 */
@Service
public class AccountTypeService {

    /**
     * Repository instance
     */
    private final AccountTypeRepository repository;

    /**
     * Constructor for the class
     * @param repository instance
     */
    @Autowired
    public AccountTypeService(AccountTypeRepository repository) {
        this.repository = repository;
    }

    /**
     * Find all account type method
     * @param userId user id
     * @return list of all account types for user
     */
    public List<AccountType> findAll(Long userId) {
        return repository.findAccountTypesByUserId(userId);
    }

    /**
     * Find account type by id method
     * @param id of account type
     * @return Optional object with found account type or empty
     */
    public Optional<AccountType> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Add new account type method
     * @param accountType new account type
     * @return saved account type
     */
    public AccountType add(AccountType accountType) {
        return repository.save(accountType);
    }

    /**
     * Update account type method
     * @param accountType current account type with new data
     * @return updated account type
     */
    public AccountType update(AccountType accountType) {
        return repository.save(accountType);
    }

    /**
     * Delete account type method
     * @param id of deleting account type
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
