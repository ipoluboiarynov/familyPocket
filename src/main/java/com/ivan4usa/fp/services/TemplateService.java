package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entity.Template;
import com.ivan4usa.fp.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {

    private TemplateRepository repository;
    @Autowired
    public TemplateService(TemplateRepository repository) {
        this.repository = repository;
    }

    public List<Template> findAll(Long userId) {
        return repository.findTemplatesByUserId(userId);
    }

    public Template findById(Long id) {
        return repository.findById(id).isPresent() ? repository.findById(id).get() : null;
    }

    public Template add(Template template) {
        return repository.save(template);
    }

    public Template update(Template template) {
        return repository.save(template);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
