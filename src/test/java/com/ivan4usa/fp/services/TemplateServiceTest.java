package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entity.Template;
import com.ivan4usa.fp.enums.RecordType;
import com.ivan4usa.fp.repository.TemplateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
class TemplateServiceTest {

    @Autowired
    private TemplateService service;

    @MockBean
    private TemplateRepository repository;

    @Test
    void findAll() {
        // Set up a mock repository
        Template template1 = new Template(1L, "Template 1", new BigDecimal("1000.00"), 5L, RecordType.EXPENSE,
                null, null);

        Template template2 = new Template(2L, "Template 2", new BigDecimal("2000.00"), 5L, RecordType.EXPENSE,
                null, null);

        Template template3 = new Template(3L, "Template 3", new BigDecimal("3000.00"), 5L, RecordType.EXPENSE,
                null, null);

        doReturn(Arrays.asList(template1, template2, template3)).when(repository).findTemplatesByUserId(5L);
        // Execute the service call
        List<Template> templates = service.findAll(5L);

        // Assert the response
        Assertions.assertEquals(3, templates.size(), "findAll should return 3 templates");
    }

    @Test
    void findById() {
        // Set up a mock repository
        Template template = new Template(5L, "Template", new BigDecimal("1000.00"), 10L, RecordType.EXPENSE,
                null, null);
        doReturn(Optional.of(template)).when(repository).findById(5L);
        // Execute the service call
        Optional<Template> returnedTemplate = service.findById(5L);
        // Assert the response
        Assertions.assertNotNull(Optional.of(returnedTemplate).get(), "Template was not found");
        Assertions.assertSame(returnedTemplate.get(), template, "The template returned was not the same as the mock");
    }

    @Test
    void testFindByIdNotFound() {
        // Set up a mock repository
        doReturn(Optional.empty()).when(repository).findById(1L);
        // Execute the service call
        Optional<Template> returnedTemplate = service.findById(1L);
        // Assert the response
        Assertions.assertFalse(returnedTemplate.isPresent(), "Template should not be found");
    }

    @Test
    void add() {
        // Set up a mock repository
        Template template = new Template(5L, "Template", new BigDecimal("1000.00"), 10L, RecordType.EXPENSE,
                null, null);
        doReturn(template).when(repository).save(any());
        // Execute the service call
        Template returnedTemplate = service.add(template);
        // Assert the response
        Assertions.assertNotNull(returnedTemplate, "The saved template should not be null");
        Assertions.assertEquals("Template", returnedTemplate.getName(), "The name should be different");
        Assertions.assertEquals(RecordType.EXPENSE, returnedTemplate.getRecordType(), "The record type should be different");
        Assertions.assertEquals(null, returnedTemplate.getAccount(), "The account should be different");
        Assertions.assertEquals(null, returnedTemplate.getCategory(), "The category should be different");
        Assertions.assertEquals(new BigDecimal("1000.00"), returnedTemplate.getAmount(), "The amount should be different");
        Assertions.assertEquals(10L, returnedTemplate.getUserId(), "The user id should be different");
    }

    @Test
    void update() {
        // Set up a mock repository
        Template updateTemplate = new Template(5L, "Template", new BigDecimal("1000.00"), 10L, RecordType.EXPENSE,
                null, null);
        doReturn(updateTemplate).when(repository).save(any());
        // Execute the service call
        Template returnedTemplate = service.update(updateTemplate);
        // Assert the response
        Assertions.assertNotNull(returnedTemplate, "The saved template should not be null");
        Assertions.assertEquals("Template", returnedTemplate.getName(), "The name should be different");
        Assertions.assertEquals(RecordType.EXPENSE, returnedTemplate.getRecordType(), "The record type should be different");
        Assertions.assertEquals(null, returnedTemplate.getAccount(), "The account should be different");
        Assertions.assertEquals(null, returnedTemplate.getCategory(), "The category should be different");
        Assertions.assertEquals(new BigDecimal("1000.00"), returnedTemplate.getAmount(), "The amount should be different");
        Assertions.assertEquals(10L, returnedTemplate.getUserId(), "The user id should be different");
    }

    @Test
    void delete() {
        // Set up a mock repository
        Template deleteTemplate = new Template(5L, "Template", new BigDecimal("1000.00"), 10L, RecordType.EXPENSE,
                null, null);
        doNothing().when(repository).deleteById(5L);
        // Execute the service call
        service.add(deleteTemplate);
        service.delete(5L);
        Optional<Template> returnedTemplate = service.findById(5L);
        // Assert the response
        Assertions.assertFalse(returnedTemplate.isPresent(), "Template should not be found");
    }
}
