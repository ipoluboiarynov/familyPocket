package com.ivan4usa.fp.services;

import com.ivan4usa.fp.repository.AccountTypeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountTypeService {
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    public AccountTypeService(AccountTypeRepository repository) {
    }
}
