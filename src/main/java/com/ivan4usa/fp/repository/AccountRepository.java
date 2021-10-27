package com.ivan4usa.fp.repository;

import com.ivan4usa.fp.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAccountsByUserId(Long userId);

    Optional<Account> findAccountById(@Param("id") Long id);

    @Query("SELECT ((case when a.startDate <= :date then a.startBalance ELSE 0 END) + " +
            "sum(CASE WHEN r.recordType = 'INCOME' and r.recordDate <= :date THEN r.amount ELSE 0 END) - " +
            "sum(case when r.recordType = 'EXPENSE' and r.recordDate <= :date then r.amount else 0 END)) " +
            "as balance from Account as a left join Record as r on a.id = r.account.id where r.account.id=:id")
    BigDecimal getBalanceByAccountId(@Param("id") Long id, @Param("date") Date date);
}
