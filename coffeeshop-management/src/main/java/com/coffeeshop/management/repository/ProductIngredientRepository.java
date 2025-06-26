package com.coffeeshop.management.repository;

import com.coffeeshop.management.model.ProductIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductIngredientRepository extends JpaRepository<ProductIngredient, Integer> {
    List<ProductIngredient> findByProduct_ProductId(Integer productId);
    List<ProductIngredient> findByIngredient_IngredientId(Integer ingredientId);
    List<ProductIngredient> findByProductId(Integer productId);
}

