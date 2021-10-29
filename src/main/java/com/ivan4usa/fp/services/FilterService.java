package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.Filter;
import com.ivan4usa.fp.repositories.FilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilterService {
    private FilterRepository repository;
    @Autowired
    public FilterService(FilterRepository repository) {
        this.repository = repository;
    }

    public List<Filter> findAll(Long userId) {
        return repository.findFiltersByUserId(userId);
    }

    public Optional<Filter> findById(Long id) {
        return repository.findById(id);
    }

    public Filter add(Filter filter) {
        return repository.save(filter);
    }

    public Filter update(Filter filter) {
        return repository.save(filter);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
