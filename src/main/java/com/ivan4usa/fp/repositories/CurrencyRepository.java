package com.ivan4usa.fp.repositories;

import com.ivan4usa.fp.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    List<Currency> findCurrenciesByUserId(Long userId);
}
