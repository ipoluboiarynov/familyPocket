package com.ivan4usa.fp.repositories;

import com.ivan4usa.fp.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAccountsByUserId(Long userId);

    Optional<Account> findAccountById(@Param("id") Long id);

    @Query(value = "SELECT ((CASE WHEN a.start_date <= :date THEN a.start_balance ELSE 0 END) + " +
            "SUM(CASE WHEN r.record_type = 'INCOME' AND r.record_date <= :date THEN r.amount ELSE 0 END) - " +
            "SUM(CASE WHEN r.record_type = 'EXPENSE' AND r.record_date <= :date THEN r.amount ELSE 0 END)) " +
            "AS balance FROM fp_db.account AS a LEFT JOIN fp_db.record AS r ON a.id = r.account_id WHERE a.id=:id",
    nativeQuery = true)
    BigDecimal getBalanceByAccountId(
            @Param("id") Long id,
            @Param("date") String date
    );
}
