package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.Record;
import com.ivan4usa.fp.enums.RecordType;
import com.ivan4usa.fp.repositories.RecordRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RecordService {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private RecordRepository repository;

    @Autowired
    public RecordService(RecordRepository repository) {
        this.repository = repository;
    }

    public List<Record> findAll(Long userId) {
        return repository.findRecordsByUserIdOrderByRecordDateDesc(userId);
    }

    public Page<Record> search(RecordType recordType, LocalDate startDate, LocalDate endDate, Long userId, List<Long> account_ids,
                               List<Long> category_ids, PageRequest pageRequest) {
        return repository.search(recordType, startDate, endDate, userId, account_ids, category_ids, pageRequest);
    }

    public Optional<Record> findById(Long id) {
        return repository.findById(id);
    }

    public Record add(Record record) {
        return repository.save(record);
    }

    @Transactional
    public Record[] addTransfer(Record recordFromAccount, Record recordToAccount) {
        Record recordFromOne = repository.save(recordFromAccount);
        Record recordToOne = repository.save(recordToAccount);
        Long idFrom = recordFromOne.getId();
        Long idTo = recordToOne.getId();
        recordFromOne.setComment(String.valueOf(idTo));
        recordToOne.setComment(String.valueOf(idFrom));
        Record recordFromTwo = repository.save(recordFromOne);
        Record recordToTwo = repository.save(recordToOne);
        Record[] result = new Record[2];
        result[0] = recordFromTwo;
        result[1] = recordToTwo;
        return result;
    }

    public Record update(Record record) {
        return repository.save(record);
    }

    @Transactional
    public void delete(Long id) {
        Optional<Record> record = repository.findById(id);
        if (record.isPresent()) {
            Record recordToBeDeleted = record.get();
            if (recordToBeDeleted.getCategory() != null) {
                 repository.deleteById(id);
            } else {
                String comment = recordToBeDeleted.getComment();
                try {
                    Long refId = Long.parseLong(comment);
                    repository.deleteById(id);
                    repository.deleteById(refId);
                } catch (Exception exception) {
                    logger.error("Trying delete record exception: " + exception);
                }
            }
        }
    }
    public Integer getTotalNumber(Long userId) {
        return repository.getTotalNumber(userId);
    }
}
