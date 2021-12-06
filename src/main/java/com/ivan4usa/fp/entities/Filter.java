package com.ivan4usa.fp.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivan4usa.fp.enums.RecordType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "filter", schema = "fp_db")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonFormat(pattern = DATE_PATTERN)
    @DateTimeFormat(pattern = DATE_PATTERN)
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonFormat(pattern = DATE_PATTERN)
    @DateTimeFormat(pattern = DATE_PATTERN)
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", length = 8)
    private RecordType recordType;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "accounts_for_filter", joinColumns = @JoinColumn(name = "filter_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> accounts = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "categories_for_filter", joinColumns = @JoinColumn(name = "filter_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();
}
