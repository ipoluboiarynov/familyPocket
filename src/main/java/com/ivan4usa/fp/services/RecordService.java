package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.Account;
import com.ivan4usa.fp.entities.Category;
import com.ivan4usa.fp.entities.Filter;
import com.ivan4usa.fp.entities.Record;
import com.ivan4usa.fp.enums.RecordType;
import com.ivan4usa.fp.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RecordService {

    private RecordRepository repository;

    @Autowired
    public RecordService(RecordRepository repository) {
        this.repository = repository;
    }

    public List<Record> findAll(Long userId) {
        return repository.findRecordsByUserIdOrderByRecordDateDesc(userId);
    }

    public Page<Record> search(RecordType recordType, Date startDate, Date endDate, Long userId, List<Long> account_ids,
                               List<Long> category_ids, PageRequest pageRequest) {
        return repository.search(recordType, startDate, endDate, userId, account_ids, category_ids, pageRequest);
    }

    public Optional<Record> findById(Long id) {
        return repository.findById(id);
    }

    public Record add(Record record) {
        return repository.save(record);
    }

    public Record update(Record record) {
        return repository.save(record);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
