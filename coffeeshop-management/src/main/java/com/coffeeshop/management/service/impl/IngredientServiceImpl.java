package com.coffeeshop.management.service.impl;

import com.coffeeshop.management.dto.IngredientDTO;
import com.coffeeshop.management.model.Category;
import com.coffeeshop.management.model.Ingredient;
import com.coffeeshop.management.repository.CategoryRepository;
import com.coffeeshop.management.repository.IngredientRepository;
import com.coffeeshop.management.service.IngredientService;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository, 
                               CategoryRepository categoryRepository) {
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public IngredientDTO createIngredient(IngredientDTO ingredientDTO) throws ResourceNotFoundException {
        validateIngredientDTO(ingredientDTO);
        
        Ingredient ingredient = new Ingredient();
        BeanUtils.copyProperties(ingredientDTO, ingredient);
        
        Category category = categoryRepository.findById(ingredientDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + ingredientDTO.getCategoryId()));
        ingredient.setCategory(category);
        
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return convertToDto(savedIngredient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientDTO> getAllIngredients() {
        return ingredientRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public IngredientDTO getIngredientById(Integer id) throws ResourceNotFoundException {
        return ingredientRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + id));
    }

    @Override
    public IngredientDTO updateIngredient(Integer id, IngredientDTO ingredientDTO) throws ResourceNotFoundException {
        validateIngredientDTO(ingredientDTO);
        
        return ingredientRepository.findById(id).map(existingIngredient -> {
            BeanUtils.copyProperties(ingredientDTO, existingIngredient, "ingredientId", "createdAt");
            
            Category category = categoryRepository.findById(ingredientDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + ingredientDTO.getCategoryId()));
            existingIngredient.setCategory(category);
            
            Ingredient updatedIngredient = ingredientRepository.save(existingIngredient);
            return convertToDto(updatedIngredient);
        }).orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + id));
    }

    @Override
    public void deleteIngredient(Integer id) throws ResourceNotFoundException {
        if (!ingredientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ingredient not found with id: " + id);
        }
        ingredientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientDTO> getIngredientsByCategory(Integer categoryId) throws ResourceNotFoundException {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        
        return ingredientRepository.findByCategory_CategoryId(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateStockQuantity(Integer ingredientId, BigDecimal quantityChange) throws ResourceNotFoundException {
        ingredientRepository.findById(ingredientId).ifPresentOrElse(
            ingredient -> {
                BigDecimal newQuantity = ingredient.getStockQuantity().add(quantityChange);
                if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("Stock quantity cannot be negative");
                }
                ingredient.setStockQuantity(newQuantity);
                ingredientRepository.save(ingredient);
            },
            () -> {
                throw new ResourceNotFoundException("Ingredient not found with id: " + ingredientId);
            }
        );
    }

    private IngredientDTO convertToDto(Ingredient ingredient) {
        IngredientDTO ingredientDTO = new IngredientDTO();
        BeanUtils.copyProperties(ingredient, ingredientDTO);
        if (ingredient.getCategory() != null) {
            ingredientDTO.setCategoryId(ingredient.getCategory().getCategoryId());
        }
        return ingredientDTO;
    }

    private void validateIngredientDTO(IngredientDTO ingredientDTO) {
        if (ingredientDTO.getIngredientName() == null || ingredientDTO.getIngredientName().trim().isEmpty()) {
            throw new IllegalArgumentException("Ingredient name cannot be empty");
        }
        if (ingredientDTO.getUnit() == null || ingredientDTO.getUnit().trim().isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be empty");
        }
        if (ingredientDTO.getStockQuantity() == null) {
            throw new IllegalArgumentException("Stock quantity cannot be null");
        }
        if (ingredientDTO.getMinStockLevel() == null) {
            throw new IllegalArgumentException("Min stock level cannot be null");
        }
    }
}
