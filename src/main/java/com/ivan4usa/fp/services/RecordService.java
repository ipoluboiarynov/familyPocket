package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entity.Record;
import com.ivan4usa.fp.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {

    private RecordRepository repository;
    @Autowired
    public RecordService(RecordRepository repository) {
        this.repository = repository;
    }

    public List<Record> findAll(Long userId) {
        return repository.findRecordsByUserId(userId);
    }

    public Record findById(Long id) {
        return repository.findById(id).isPresent() ? repository.findById(id).get() : null;
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
