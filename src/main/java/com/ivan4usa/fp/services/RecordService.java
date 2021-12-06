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

/**
 * The service that is called from the corresponding controller and controls actions on the Record
 */
@Service
public class RecordService {

    /**
     * Instance of log Manager
     */
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Repository instance
     */
    private final RecordRepository repository;

    /**
     * Constructor for the class
     * @param repository instance
     */
    @Autowired
    public RecordService(RecordRepository repository) {
        this.repository = repository;
    }

    /**
     * Find all record by user id method
     * @param userId user id
     * @return all found records by user id
     */
    public List<Record> findAll(Long userId) {
        return repository.findRecordsByUserIdOrderByRecordDateDesc(userId);
    }

    /**
     * Search records by parameters method that returns page of found records
     * @param recordType record type search parameter
     * @param startDate start date search parameter
     * @param endDate end date search parameter
     * @param userId user id search parameter
     * @param accountIds list of account ids search parameter
     * @param categoryIds list of category ids search parameter
     * @param pageRequest page settings parameter
     * @return page with found records
     */
    public Page<Record> search(RecordType recordType, LocalDate startDate, LocalDate endDate, Long userId, List<Long> accountIds,
                               List<Long> categoryIds, PageRequest pageRequest) {
        return repository.search(recordType, startDate, endDate, userId, accountIds, categoryIds, pageRequest);
    }

    /**
     * Optional object with found record or empty
     * @param id of record
     * @return found record by id
     */
    public Optional<Record> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Add new record method
     * @param record to be added
     * @return added record
     */
    public Record add(Record record) {
        return repository.save(record);
    }

    /**
     * Transactional method that adds two records of transfer between accounts of user
     * @param recordFromAccount record for account from which transaction moves
     * @param recordToAccount record for account to which transaction moves
     * @return array with added records
     */
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

    /**
     * Update record method
     * @param record with new data
     * @return updated record
     */
    public Record update(Record record) {
        return repository.save(record);
    }

    /**
     * Delete record by id method that also checks if the deleting record has a category, if it is not, than it's getting
     * related record's id from comment field of current record (that should be there) and also deletes that related method
     * since it is related record of the same transfer operation
     * @param id of deleting record
     */
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

    /**
     * Method that gets total number of records by user id
     * @param userId user id
     * @return number of total records
     */
    public Integer getTotalNumber(Long userId) {
        return repository.getTotalNumber(userId);
    }
}
