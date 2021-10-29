package com.ivan4usa.fp.repositories;

import com.ivan4usa.fp.entities.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository  extends JpaRepository<Template, Long> {
    List<Template> findTemplatesByUserId(Long userId);
}
