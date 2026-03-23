package com.demo.userapi.repository;

import com.demo.userapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // intentionally empty — no useful query methods
}