package com.kedaya.springboot3mongodb.repository;

import com.kedaya.springboot3mongodb.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByCategory(String category);
    
    List<Product> findByCategoryAndPriceGreaterThan(String category, BigDecimal price);
    
    // 使用MongoDB原生查询
    @Query("{'category': ?0, 'price': {$lt: ?1}}")
    List<Product> findByCategoryAndPriceLessThan(String category, BigDecimal price);
} 