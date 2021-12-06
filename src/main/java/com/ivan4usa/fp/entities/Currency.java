package com.ivan4usa.fp.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "currency", schema = "fp_db")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 3)
    private String name;

    @Column(name = "icon", length = 50)
    private String icon;

    @Column(name = "base")
    private boolean base;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
