package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.Category;
import com.ivan4usa.fp.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> findAll(Long userId) {

        return repository.findCategoriesByUserId(userId);
    }

    public Optional<Category> findById(Long id) {
        return repository.findById(id);
    }

    public Category add(Category category) {
        return repository.save(category);
    }

    public Category update(Category category) {
        return repository.save(category);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
