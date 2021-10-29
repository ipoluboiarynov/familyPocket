package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.Category;
import com.ivan4usa.fp.repositories.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
class CategoryServiceTest {

    @Autowired
    private CategoryService service;

    @MockBean
    private CategoryRepository repository;

    @Test
    void findAll() {
        // Set up a mock repository
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");
        category1.setIcon("icon 1");
        category1.setExpense(true);
        category1.setUserId(2L);

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");
        category2.setIcon("icon 2");
        category2.setExpense(false);
        category2.setUserId(2L);

        Category category3 = new Category();
        category3.setId(3L);
        category3.setName("Category 3");
        category3.setIcon("icon 3");
        category3.setExpense(true);
        category3.setUserId(2L);
        doReturn(Arrays.asList(category1, category2, category3)).when(repository).findCategoriesByUserId(2L);
        // Execute the service call
        List<Category> categoryList = service.findAll(2L);
        // Assert the response
        Assertions.assertEquals(3, categoryList.size(), "findAll should return 3 categories");
    }

    @Test
    void findById() {
        // Set up a mock repository
        Category category = new Category();
        category.setId(4L);
        category.setName("Category 4");
        category.setIcon("icon 4");
        category.setExpense(true);
        category.setUserId(1L);
        doReturn(Optional.of(category)).when(repository).findById(4L);
        // Execute the service call
        Optional<Category> returnedCategory = service.findById(4L);
        // Assert the response
        Assertions.assertNotNull(Optional.of(returnedCategory).get(), "The category was not found");
        Assertions.assertSame(returnedCategory.get(), category, "The category returned was not the same as the mock");
    }

    @Test
    void testFindByIdNotFound() {
        // Set up a mock repository
        doReturn(Optional.empty()).when(repository).findById(1L);
        // Execute the service call
        Optional<Category> returnedCategory = service.findById(1L);
        // Assert the response
        Assertions.assertFalse(returnedCategory.isPresent(), "Category should not be found");
    }

    @Test
    void add() {
        // Set up a mock repository
        Category newCategory = new Category();
        newCategory.setId(10L);
        newCategory.setName("New Category");
        newCategory.setIcon("icon");
        newCategory.setExpense(false);
        newCategory.setUserId(4L);
        doReturn(newCategory).when(repository).save(any());
        // Execute the service call
        Category returnedCategory = service.add(newCategory);
        // Assert the response
        Assertions.assertNotNull(returnedCategory, "The saved category should not be null");
        Assertions.assertEquals("New Category", returnedCategory.getName(), "The name should be different");
        Assertions.assertEquals("icon", returnedCategory.getIcon(), "The icon should be different");
        Assertions.assertEquals(false, returnedCategory.isExpense(), "The expense property value should be different");
        Assertions.assertEquals(4L, returnedCategory.getUserId(), "The user id should be different");
    }

    @Test
    void update() {
        // Set up a mock repository
        Category updateCategory = new Category();
        updateCategory.setId(8L);
        updateCategory.setName("Update Category");
        updateCategory.setIcon("icon");
        updateCategory.setExpense(true);
        updateCategory.setUserId(2L);
        doReturn(updateCategory).when(repository).save(any());
        // Execute the service call
        Category returnedCategory = service.update(updateCategory);
        // Assert the response
        Assertions.assertNotNull(returnedCategory, "The saved category should not be null");
        Assertions.assertEquals("Update Category", returnedCategory.getName(), "The name should be different");
        Assertions.assertEquals("icon", returnedCategory.getIcon(), "The icon should be different");
        Assertions.assertEquals(true, returnedCategory.isExpense(), "The expense property value should not be different");
        Assertions.assertEquals(2L, returnedCategory.getUserId(), "The user id should not be different");
    }

    @Test
    void delete() {
        // Set up a mock repository
        Category deleteCategory = new Category();
        deleteCategory.setId(3L);
        deleteCategory.setName("Delete Category");
        deleteCategory.setIcon("icon");
        deleteCategory.setExpense(true);
        deleteCategory.setUserId(1L);
        doNothing().when(repository).deleteById(3L);
        // Execute the service call
        service.add(deleteCategory);
        service.delete(3L);
        Optional<Category> returnedCategory = service.findById(1L);
        // Assert the response
        Assertions.assertFalse(returnedCategory.isPresent(), "Category should not be found");
    }
}
