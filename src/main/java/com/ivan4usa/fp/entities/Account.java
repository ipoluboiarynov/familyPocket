package com.ivan4usa.fp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "account", schema = "fp_db")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "icon", nullable = false, length = 50)
    private String icon;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "color", nullable = false, length = 7)
    private String color;

    @Column(name = "credit_limit", nullable = false, precision = 2)
    private BigDecimal creditLimit;

    @Column(name = "start_balance", nullable = false, precision = 2)
    private BigDecimal startBalance;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "account_type_id", referencedColumnName = "id", nullable = false)
    private AccountType accountType;

    @ManyToOne
    @JoinColumn(name = "currency_id", referencedColumnName = "id", nullable = false)
    private Currency currency;

    @Transient
    private BigDecimal balance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }


}
