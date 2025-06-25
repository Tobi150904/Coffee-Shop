package com.coffeeshop.management.service.impl;

import com.coffeeshop.management.dto.CategoryDTO;
import com.coffeeshop.management.model.Category;
import com.coffeeshop.management.repository.CategoryRepository;
import com.coffeeshop.management.service.CategoryService;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setType(Category.CategoryType.valueOf(categoryDTO.getType().toUpperCase()));
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Integer id) throws ResourceNotFoundException {
        return categoryRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Override
    public CategoryDTO updateCategory(Integer id, CategoryDTO categoryDTO) throws ResourceNotFoundException {
        return categoryRepository.findById(id).map(existingCategory -> {
            BeanUtils.copyProperties(categoryDTO, existingCategory, "categoryId");
            existingCategory.setType(Category.CategoryType.valueOf(categoryDTO.getType().toUpperCase()));
            Category updatedCategory = categoryRepository.save(existingCategory);
            return convertToDto(updatedCategory);
        }).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Override
    public void deleteCategory(Integer id) throws ResourceNotFoundException {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoriesByType(String type) {
        return categoryRepository.findByType(Category.CategoryType.valueOf(type.toUpperCase())).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CategoryDTO convertToDto(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, categoryDTO);
        categoryDTO.setType(category.getType().name());
        return categoryDTO;
    }
}
