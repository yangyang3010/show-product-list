package com.example.demo.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.example.demo.dtos.ProductDTO;
import com.example.demo.models.Product;
import com.example.demo.service.ProductService;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createProduct(@ModelAttribute ProductDTO productDTO) {
        try {
            // Lưu sản phẩm
            Product newProduct = productService.createProduct(productDTO);

            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<>() : files;

            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                if(file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large");
                }
            }

            return ResponseEntity.ok("Product created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    //public ResponseEntity<String> getProducts(
    public ResponseEntity<Map<String, Object>> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        Page<Product> productPage = productService.getAllProducts(PageRequest.of(page, limit));

        // Tạo phản hồi
        Map<String, Object> response = new HashMap<>();
        response.put("products", productPage.getContent());
        response.put("currentPage", productPage.getNumber());
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable("id") Long productId
    ) {
        try {
            Product existingProduct = productService.getProductById(productId);
            return  ResponseEntity.ok(existingProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(String.format("Product deleted with id = %d successfully", id));
    }

    //@PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();
        for (int i = 0; i < 50; i++) {
            String productName = faker.commerce().productName();
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10, 500))
                    .thumbnail("")
                    .categoryId((long)faker.number().numberBetween(1, 5))
                    .build();
            productService.createProduct(productDTO);
        }
        return ResponseEntity.ok("Fake products created successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam String name,
            @RequestParam int page,
            @RequestParam int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit); // -1 vì page bắt đầu từ 0
        Page<Product> products = productService.searchProducts(name, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<Page<Product>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        Pageable pageable = PageRequest.of(page, limit);
        Page<Product> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category")
    public Page<Product> getProductsByCategory(@RequestParam Long categoryId, Pageable pageable) {
        return productService.getProductsByCategory(categoryId, pageable);
    }
}
