package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.Category;
import com.ivan4usa.fp.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The service that is called from the corresponding controller and controls actions on the Category
 */
@Service
public class CategoryService {

    /**
     * Repository instance
     */
    private final CategoryRepository repository;

    /**
     * Constructor for the class
     * @param repository instance
     */
    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Find all categories by user id method
     * @param userId user id
     * @return list of all found categories for user
     */
    public List<Category> findAll(Long userId) {
        return repository.findCategoriesByUserId(userId);
    }

    /**
     * FInd category by id method
     * @param id of category
     * @return Optional object with found category or empty
     */
    public Optional<Category> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Add new category method
     * @param category to be added
     * @return added category
     */
    public Category add(Category category) {
        return repository.save(category);
    }

    /**
     * Update category method
     * @param category with new data
     * @return updated category
     */
    public Category update(Category category) {
        return repository.save(category);
    }

    /**
     * Delete category method
     * @param id of deleting category
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
