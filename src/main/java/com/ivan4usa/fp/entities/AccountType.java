package com.ivan4usa.fp.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "account_type", schema = "fp_db")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AccountType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "negative")
    private boolean negative;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
