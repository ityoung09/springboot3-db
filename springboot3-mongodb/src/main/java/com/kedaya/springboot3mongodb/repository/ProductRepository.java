package com.kedaya.springboot3mongodb.repository;

import com.kedaya.springboot3mongodb.model.entity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends MongoRepository<ProductEntity, String> {

    List<ProductEntity> findByCategory(String category);
    
    List<ProductEntity> findByCategoryAndPriceGreaterThan(String category, BigDecimal price);
    
    // 使用MongoDB原生查询
    @Query("{'category': ?0, 'price': {$lt: ?1}}")
    List<ProductEntity> findByCategoryAndPriceLessThan(String category, BigDecimal price);
} 