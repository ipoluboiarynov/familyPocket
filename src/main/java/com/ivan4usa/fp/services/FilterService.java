package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.Filter;
import com.ivan4usa.fp.repositories.FilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The service that is called from the corresponding controller and controls actions on the Filter
 */
@Service
public class FilterService {

    /**
     * Repository instance
     */
    private final FilterRepository repository;

    /**
     * Constructor for the class
     * @param repository instance
     */
    @Autowired
    public FilterService(FilterRepository repository) {
        this.repository = repository;
    }

    /**
     * Find all filters by user id method
     * @param userId user id
     * @return all found filters by user id
     */
    public List<Filter> findAll(Long userId) {
        return repository.findFiltersByUserId(userId);
    }

    /**
     * Find filter by id method
     * @param id of filter
     * @return Optional object with found filter or empty
     */
    public Optional<Filter> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Sdd new filter method
     * @param filter to be added
     * @return added filter
     */
    public Filter add(Filter filter) {
        return repository.save(filter);
    }

    /**
     * Update filter method
     * @param filter with new data
     * @return updated filter
     */
    public Filter update(Filter filter) {
        return repository.save(filter);
    }

    /**
     * Delete filter by id method
     * @param id of deleting filter
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
