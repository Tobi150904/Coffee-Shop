package com.coffeeshop.management.repository;

import com.coffeeshop.management.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    List<Ingredient> findByCategory_CategoryId(Integer categoryId);
}