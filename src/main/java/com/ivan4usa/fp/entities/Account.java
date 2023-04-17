package com.ivan4usa.fp.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "account", schema = "fp_db")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "icon", nullable = false, length = 50)
    private String icon;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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



}
