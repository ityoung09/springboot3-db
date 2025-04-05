package com.kedaya.springboot3mongodb.controller;

import com.kedaya.springboot3mongodb.model.entity.ProductEntity;
import com.kedaya.springboot3mongodb.repository.ProductRepository;
import com.kedaya.springboot3mongodb.service.impl.ProductAggregationServiceImpl;
import com.kedaya.springboot3mongodb.service.impl.TransactionalProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 产品控制器
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductAggregationServiceImpl aggregationService;
    
    @Autowired
    private TransactionalProductServiceImpl transactionalService;
    
    /**
     * 创建新产品
     */
    @PostMapping
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity productEntity) {
        ProductEntity savedProductEntity = productRepository.save(productEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductEntity);
    }
    
    /**
     * 查询所有产品
     */
    @GetMapping
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }
    
    /**
     * 按ID查询产品
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getProductById(@PathVariable String id) {
        Optional<ProductEntity> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 按类别查询产品
     */
    @GetMapping("/category/{category}")
    public List<ProductEntity> getProductsByCategory(@PathVariable String category) {
        return productRepository.findByCategory(category);
    }
    
    /**
     * 按类别和最低价格查询产品
     */
    @GetMapping("/category/{category}/minPrice/{price}")
    public List<ProductEntity> getProductsByCategoryAndMinPrice(
            @PathVariable String category,
            @PathVariable BigDecimal price) {
        return productRepository.findByCategoryAndPriceGreaterThan(category, price);
    }
    
    /**
     * 更新产品
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductEntity> updateProduct(
            @PathVariable String id, 
            @RequestBody ProductEntity productEntity) {
        
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        productEntity.setId(id);
        ProductEntity updatedProductEntity = productRepository.save(productEntity);
        
        return ResponseEntity.ok(updatedProductEntity);
    }
    
    /**
     * 删除产品
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 聚合查询：按类别获取平均价格
     */
    @GetMapping("/aggregation/avgPriceByCategory")
    public List<Map> getAveragePriceByCategory() {
        return aggregationService.getAveragePriceByCategory();
    }
    
    /**
     * 聚合查询：获取某类别中最贵的N个产品
     */
    @GetMapping("/aggregation/topPriced/{category}/{limit}")
    public List<ProductEntity> getTopPricedProductsByCategory(
            @PathVariable String category,
            @PathVariable int limit) {
        return aggregationService.getTopPricedProductsByCategory(category, limit);
    }
    
    /**
     * 聚合查询：按价格范围分组统计产品数量
     */
    @GetMapping("/aggregation/countByPriceRange")
    public List<Map> countProductsByPriceRange() {
        return aggregationService.countProductsByPriceRange();
    }
    
    /**
     * 使用事务批量创建产品
     */
    @PostMapping("/batch")
    public ResponseEntity<List<ProductEntity>> batchCreateProducts(@RequestBody List<ProductEntity> productEntities) {
        List<ProductEntity> savedProductEntities = transactionalService.batchCreateProducts(productEntities);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductEntities);
    }
    
    /**
     * 使用事务更新多个产品的价格
     */
    @PutMapping("/batch/price/{amount}")
    public ResponseEntity<Void> updateProductPrices(
            @RequestBody List<String> productIds,
            @PathVariable BigDecimal amount) {
        
        transactionalService.updateProductPrices(productIds, amount);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 使用事务替换产品
     */
    @PutMapping("/replace/{oldProductId}")
    public ResponseEntity<ProductEntity> replaceProduct(
            @PathVariable String oldProductId,
            @RequestBody ProductEntity newProductEntity) {
        
        if (!productRepository.existsById(oldProductId)) {
            return ResponseEntity.notFound().build();
        }
        
        ProductEntity replacedProductEntity = transactionalService.replaceProduct(oldProductId, newProductEntity);
        return ResponseEntity.ok(replacedProductEntity);
    }
} 