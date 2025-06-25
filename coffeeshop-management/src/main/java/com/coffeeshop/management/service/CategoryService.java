package com.coffeeshop.management.service;

import com.coffeeshop.management.dto.CategoryDTO;
import com.coffeeshop.management.exception.ResourceNotFoundException;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Integer id) throws ResourceNotFoundException;
    CategoryDTO updateCategory(Integer id, CategoryDTO categoryDTO) throws ResourceNotFoundException;
    void deleteCategory(Integer id) throws ResourceNotFoundException;
    List<CategoryDTO> getCategoriesByType(String type);
}