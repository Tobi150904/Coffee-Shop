package com.coffeeshop.management.service;

import com.coffeeshop.management.dto.ProductDTO;
import com.coffeeshop.management.exception.ResourceNotFoundException;
import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO) throws ResourceNotFoundException;
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Integer id) throws ResourceNotFoundException;
    ProductDTO updateProduct(Integer id, ProductDTO productDTO) throws ResourceNotFoundException;
    void deleteProduct(Integer id) throws ResourceNotFoundException;
    List<ProductDTO> getProductsByCategoryId(Integer categoryId) throws ResourceNotFoundException;
    List<ProductDTO> searchProducts(String keyword);
}