package com.coffeeshop.management.service.impl;

import com.coffeeshop.management.dto.ProductIngredientDTO;
import com.coffeeshop.management.model.Ingredient;
import com.coffeeshop.management.model.Product;
import com.coffeeshop.management.model.ProductIngredient;
import com.coffeeshop.management.repository.IngredientRepository;
import com.coffeeshop.management.repository.ProductIngredientRepository;
import com.coffeeshop.management.repository.ProductRepository;
import com.coffeeshop.management.service.ProductIngredientService;
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
public class ProductIngredientServiceImpl implements ProductIngredientService {

    private final ProductIngredientRepository productIngredientRepository;
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public ProductIngredientServiceImpl(ProductIngredientRepository productIngredientRepository,
                                      ProductRepository productRepository,
                                      IngredientRepository ingredientRepository) {
        this.productIngredientRepository = productIngredientRepository;
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public ProductIngredientDTO createProductIngredient(ProductIngredientDTO productIngredientDTO) throws ResourceNotFoundException {
        validateProductIngredientDTO(productIngredientDTO);
        
        ProductIngredient productIngredient = new ProductIngredient();
        BeanUtils.copyProperties(productIngredientDTO, productIngredient);
        
        Product product = productRepository.findById(productIngredientDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productIngredientDTO.getProductId()));
        productIngredient.setProduct(product);
        
        Ingredient ingredient = ingredientRepository.findById(productIngredientDTO.getIngredientId())
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + productIngredientDTO.getIngredientId()));
        productIngredient.setIngredient(ingredient);
        
        ProductIngredient savedProductIngredient = productIngredientRepository.save(productIngredient);
        return convertToDto(savedProductIngredient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductIngredientDTO> getAllProductIngredients() {
        return productIngredientRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductIngredientDTO getProductIngredientById(Integer id) throws ResourceNotFoundException {
        return productIngredientRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("ProductIngredient not found with id: " + id));
    }

    @Override
    public ProductIngredientDTO updateProductIngredient(Integer id, ProductIngredientDTO productIngredientDTO) throws ResourceNotFoundException {
        validateProductIngredientDTO(productIngredientDTO);
        
        return productIngredientRepository.findById(id).map(existingProductIngredient -> {
            BeanUtils.copyProperties(productIngredientDTO, existingProductIngredient, "productIngredientId");
            
            Product product = productRepository.findById(productIngredientDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productIngredientDTO.getProductId()));
            existingProductIngredient.setProduct(product);
            
            Ingredient ingredient = ingredientRepository.findById(productIngredientDTO.getIngredientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + productIngredientDTO.getIngredientId()));
            existingProductIngredient.setIngredient(ingredient);
            
            ProductIngredient updatedProductIngredient = productIngredientRepository.save(existingProductIngredient);
            return convertToDto(updatedProductIngredient);
        }).orElseThrow(() -> new ResourceNotFoundException("ProductIngredient not found with id: " + id));
    }

    @Override
    public void deleteProductIngredient(Integer id) throws ResourceNotFoundException {
        if (!productIngredientRepository.existsById(id)) {
            throw new ResourceNotFoundException("ProductIngredient not found with id: " + id);
        }
        productIngredientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductIngredientDTO> getProductIngredientsByProductId(Integer productId) throws ResourceNotFoundException {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        
        return productIngredientRepository.findByProduct_ProductId(productId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductIngredientDTO> getProductIngredientsByIngredientId(Integer ingredientId) throws ResourceNotFoundException {
        if (!ingredientRepository.existsById(ingredientId)) {
            throw new ResourceNotFoundException("Ingredient not found with id: " + ingredientId);
        }
        
        return productIngredientRepository.findByIngredient_IngredientId(ingredientId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProductIngredientDTO convertToDto(ProductIngredient productIngredient) {
        ProductIngredientDTO productIngredientDTO = new ProductIngredientDTO();
        BeanUtils.copyProperties(productIngredient, productIngredientDTO);
        if (productIngredient.getProduct() != null) {
            productIngredientDTO.setProductId(productIngredient.getProduct().getProductId());
        }
        if (productIngredient.getIngredient() != null) {
            productIngredientDTO.setIngredientId(productIngredient.getIngredient().getIngredientId());
        }
        return productIngredientDTO;
    }

    private void validateProductIngredientDTO(ProductIngredientDTO productIngredientDTO) {
        if (productIngredientDTO.getProductId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (productIngredientDTO.getIngredientId() == null) {
            throw new IllegalArgumentException("Ingredient ID cannot be null");
        }
        if (productIngredientDTO.getQuantityNeeded() == null || 
            productIngredientDTO.getQuantityNeeded().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity needed must be greater than zero");
        }
    }
}