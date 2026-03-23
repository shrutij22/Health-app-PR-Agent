package com.demo.userapi.controller;

import com.demo.userapi.model.Product;
import com.demo.userapi.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
  
    //private static final String DB_PASSWORD = "admin3";
    //private static final String SECRET_KEY = "mySecretKey123";
    //private static final String API_KEY = "sk-1234567890abcdef";

    // FIXED: constructor injection instead of field injection
    private final ProductService productService;

    // FIXED: added @Valid to validate input
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // FIXED: returns 404 if not found instead of null
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // FIXED: returns 204 No Content on delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // FIXED: added validation — discount must be between 0 and 100-
    @PostMapping("/{id}/discount")
    public ResponseEntity<Double> applyDiscount(
            @PathVariable Long id,
            @RequestParam double discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.applyDiscount(id, discountPercent));
    }
}
