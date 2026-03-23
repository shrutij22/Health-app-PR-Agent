package com.demo.userapi.service;
 
import com.demo.userapi.model.Product;
import com.demo.userapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
@RequiredArgsConstructor
public class ProductService {
 
    // FIXED: constructor injection instead of field injection
    private final ProductRepository productRepository;
 
    // FIXED: input is now validated via @Valid in controller
    public Product createProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return productRepository.save(product);
    }
 
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
 
    // FIXED: throws exception instead of returning null
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
 
    // FIXED: checks if product exists before delete
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
 
    // FIXED: validation added for discount percentage
    public double applyDiscount(Long id, double discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        double discounted = product.getPrice() - (product.getPrice() * discountPercent / 100);
        product.setPrice(discounted);
        productRepository.save(product);
        return discounted;
    }
}
