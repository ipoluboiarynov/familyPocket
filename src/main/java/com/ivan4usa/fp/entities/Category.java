package com.ivan4usa.fp.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "category", schema = "fp_db")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "icon", nullable = false, length = 50)
    private String icon;

    @Column(name = "is_expense")
    private boolean isExpense;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
