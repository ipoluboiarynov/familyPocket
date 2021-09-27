package com.ivan4usa.fp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan4usa.fp.entity.Category;
import com.ivan4usa.fp.services.CategoryService;
import com.ivan4usa.fp.services.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @MockBean
    private CategoryService service;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        // Set up a mocked service
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");
        category1.setIcon("icon1");
        category1.setExpense(true);
        category1.setUserId(3L);

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");
        category2.setIcon("icon2");
        category2.setExpense(false);
        category2.setUserId(3L);

        when(userService.getUserId()).thenReturn(3L);
        doReturn(Lists.newArrayList(category1, category2)).when(service).findAll(3L);
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/category/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(3L)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Category 1")))
                .andExpect(jsonPath("$[0].icon", is("icon1")))
                .andExpect(jsonPath("$[0].expense", is(true)))
                .andExpect(jsonPath("$[0].userId", is(3)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Category 2")))
                .andExpect(jsonPath("$[1].icon", is("icon2")))
                .andExpect(jsonPath("$[1].expense", is(false)))
                .andExpect(jsonPath("$[1].userId", is(3)));
    }

    @Test
    void findById() throws Exception {
        // Set up a mocked service
        Category category = new Category();
        category.setId(15L);
        category.setName("Category");
        category.setIcon("icon");
        category.setExpense(true);
        category.setUserId(5L);

        when(userService.getUserId()).thenReturn(5L);
        doReturn(Optional.of(category)).when(service).findById(15L);
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/category/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(15L)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(15)))
                .andExpect(jsonPath("$.name", is("Category")))
                .andExpect(jsonPath("$.icon", is("icon")))
                .andExpect(jsonPath("$.expense", is(true)))
                .andExpect(jsonPath("$.userId", is(5)));
    }

    @Test
    void add() throws Exception {
        // Set up a mocked service
        Category categoryPost = new Category();
        categoryPost.setId(null);
        categoryPost.setName("Category post");
        categoryPost.setIcon("icon");
        categoryPost.setExpense(true);
        categoryPost.setUserId(2L);

        Category categoryReturn = new Category();
        categoryReturn.setId(3L);
        categoryReturn.setName("Category post");
        categoryReturn.setIcon("icon");
        categoryReturn.setExpense(true);
        categoryReturn.setUserId(2L);

        when(userService.getUserId()).thenReturn(2L);
        doReturn(categoryReturn).when(service).add(any());
        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(categoryPost)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Category post")))
                .andExpect(jsonPath("$.icon", is("icon")))
                .andExpect(jsonPath("$.expense", is(true)))
                .andExpect(jsonPath("$.userId", is(2)));
    }

    @Test
    void update() throws Exception {
        // Set up a mocked service
        Category categoryPatch = new Category();
        categoryPatch.setId(3L);
        categoryPatch.setName("Category patch");
        categoryPatch.setIcon("icon");
        categoryPatch.setExpense(true);
        categoryPatch.setUserId(4L);

        Category categoryFoundById = new Category();
        categoryFoundById.setId(3L);
        categoryFoundById.setName("Category patch");
        categoryFoundById.setIcon("icon");
        categoryFoundById.setExpense(true);
        categoryFoundById.setUserId(4L);

        Category categoryReturn = new Category();
        categoryReturn.setId(3L);
        categoryReturn.setName("Category patch");
        categoryReturn.setIcon("icon");
        categoryReturn.setExpense(true);
        categoryReturn.setUserId(4L);

        when(service.findById(3L)).thenReturn(Optional.of(categoryFoundById));
        when(userService.getUserId()).thenReturn(4L);
        doReturn(categoryReturn).when(service).update(any());

        // Execute the POST request
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/category/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(categoryPatch)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Category patch")))
                .andExpect(jsonPath("$.icon", is("icon")))
                .andExpect(jsonPath("$.expense", is(true)))
                .andExpect(jsonPath("$.userId", is(4)));
    }

    @Test
    void delete() throws Exception {
        // Set up a mocked service
        Category categoryDelete = new Category();
        categoryDelete.setId(2L);
        categoryDelete.setName("Category delete");
        categoryDelete.setIcon("icon delete");
        categoryDelete.setExpense(false);
        categoryDelete.setUserId(5L);

        when(userService.getUserId()).thenReturn(5L);
        doNothing().when(service).delete(any());
        // Execute the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/category/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(categoryDelete)))
                // Validate the response code and content type
                .andExpect(status().isOk());
        // Validate the service usage
        verify(service, times(1)).delete(2L);
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
