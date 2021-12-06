package com.ivan4usa.fp.repositories;

import com.ivan4usa.fp.entities.Record;
import com.ivan4usa.fp.enums.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findRecordsByUserIdOrderByRecordDateDesc(Long userId);

    @Query("select r from Record r where r.userId = :userId and " +
            "(:recordType is null or r.recordType=:recordType) and " +
            "(cast(:startDate as timestamp) is null or r.recordDate>=:startDate) and " +
            "(cast(:endDate as timestamp) is null or r.recordDate<=:endDate) and " +
            "((:accountIds) is null or r.account.id in (:accountIds)) and " +
            "((:categoryIds) is null or r.category.id in (:categoryIds)) " +
            "order by r.recordDate desc, r.id desc"
    )
    Page<Record> search(@Param("recordType") RecordType recordType,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("userId") Long userId,
                        @Param("accountIds") List<Long> accountIds,
                        @Param("categoryIds") List<Long> categoryIds,
                        Pageable pageable
    );

    @Query("select count(*) from Record r where r.userId = :userId")
    Integer getTotalNumber(@Param("userId") Long userId);
}
