package com.ivan4usa.fp.repository;

import com.ivan4usa.fp.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository  extends JpaRepository<Template, Long> {
}
