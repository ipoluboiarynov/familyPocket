package com.ivan4usa.fp.repositories;

import com.ivan4usa.fp.entities.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findRecordsByUserId(Long userId);
}
