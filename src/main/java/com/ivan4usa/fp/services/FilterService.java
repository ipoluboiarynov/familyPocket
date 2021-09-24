package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entity.Filter;
import com.ivan4usa.fp.repository.FilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Filter findById(Long id) {
        return repository.findById(id).isPresent() ? repository.findById(id).get() : null;
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
