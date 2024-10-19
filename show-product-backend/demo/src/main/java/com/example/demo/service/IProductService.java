package com.example.demo.service;

import com.example.demo.dtos.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.example.demo.models.*;
import org.springframework.data.domain.Pageable;

public interface IProductService {
//    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    public Product createProduct(ProductDTO productDTO);
    Product getProductById(long id) throws Exception;
    //Page<Product> getAllProducts(PageRequest pageRequest);
    Page<Product> getAllProducts(Pageable pageable);
    Product updateProduct(long id, ProductDTO productDTO) throws Exception;
    void deleteProduct(long id);
    boolean existsByName(String name);
    Page<Product> searchProducts(String name, Pageable pageable);
    Page<Product> getProductsByCategory(Long categoryId, Pageable pageable);
}
