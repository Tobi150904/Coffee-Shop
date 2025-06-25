package com.coffeeshop.management.service;

import com.coffeeshop.management.dto.ProductIngredientDTO;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import java.util.List;

public interface ProductIngredientService {
    ProductIngredientDTO createProductIngredient(ProductIngredientDTO productIngredientDTO) throws ResourceNotFoundException;
    List<ProductIngredientDTO> getAllProductIngredients();
    ProductIngredientDTO getProductIngredientById(Integer id) throws ResourceNotFoundException;
    ProductIngredientDTO updateProductIngredient(Integer id, ProductIngredientDTO productIngredientDTO) throws ResourceNotFoundException;
    void deleteProductIngredient(Integer id) throws ResourceNotFoundException;
    List<ProductIngredientDTO> getProductIngredientsByProductId(Integer productId) throws ResourceNotFoundException;
    List<ProductIngredientDTO> getProductIngredientsByIngredientId(Integer ingredientId) throws ResourceNotFoundException;
}