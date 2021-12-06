package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.Template;
import com.ivan4usa.fp.repositories.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The service that is called from the corresponding controller and controls actions on the Template
 */
@Service
public class TemplateService {

    /**
     * Repository instance
     */
    private TemplateRepository repository;

    /**
     * Constructor for the class
     * @param repository instance
     */
    @Autowired
    public TemplateService(TemplateRepository repository) {
        this.repository = repository;
    }

    /**
     * Find all templates by user id method
     * @param userId
     * @return
     */
    public List<Template> findAll(Long userId) {
        return repository.findTemplatesByUserId(userId);
    }

    /**
     * Find template by user id method
     * @param id of template
     * @return Optional object with found template or empty
     */
    public Optional<Template> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Add new template method that returns saved template
     * @param template to be saved
     * @return saved template
     */
    public Template add(Template template) {
        return repository.save(template);
    }

    /**
     * Update current template method
     * @param template template with new data
     * @return updated template
     */
    public Template update(Template template) {
        return repository.save(template);
    }

    /**
     * Delete template by id method
     * @param id of deleting template
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
