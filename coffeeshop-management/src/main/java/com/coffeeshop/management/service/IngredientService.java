package com.coffeeshop.management.service;

import com.coffeeshop.management.dto.IngredientDTO;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public interface IngredientService {
    IngredientDTO createIngredient(IngredientDTO ingredientDTO) throws ResourceNotFoundException;
    List<IngredientDTO> getAllIngredients();
    IngredientDTO getIngredientById(Integer id) throws ResourceNotFoundException;
    IngredientDTO updateIngredient(Integer id, IngredientDTO ingredientDTO) throws ResourceNotFoundException;
    void deleteIngredient(Integer id) throws ResourceNotFoundException;
    List<IngredientDTO> getIngredientsByCategory(Integer categoryId) throws ResourceNotFoundException;
    void updateStockQuantity(Integer ingredientId, BigDecimal quantityChange) throws ResourceNotFoundException;
}
