package com.ivan4usa.fp.repository;

import com.ivan4usa.fp.entity.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {
    List<Filter> findFiltersByUserId(Long userId);
}
