package com.kedaya.springboot3mongodb.controller;

import com.kedaya.springboot3mongodb.model.Product;
import com.kedaya.springboot3mongodb.repository.ProductRepository;
import com.kedaya.springboot3mongodb.service.ProductAggregationService;
import com.kedaya.springboot3mongodb.service.TransactionalProductService;
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
    private ProductAggregationService aggregationService;
    
    @Autowired
    private TransactionalProductService transactionalService;
    
    /**
     * 创建新产品
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }
    
    /**
     * 查询所有产品
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    /**
     * 按ID查询产品
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 按类别查询产品
     */
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productRepository.findByCategory(category);
    }
    
    /**
     * 按类别和最低价格查询产品
     */
    @GetMapping("/category/{category}/minPrice/{price}")
    public List<Product> getProductsByCategoryAndMinPrice(
            @PathVariable String category,
            @PathVariable BigDecimal price) {
        return productRepository.findByCategoryAndPriceGreaterThan(category, price);
    }
    
    /**
     * 更新产品
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable String id, 
            @RequestBody Product product) {
        
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        product.setId(id);
        Product updatedProduct = productRepository.save(product);
        
        return ResponseEntity.ok(updatedProduct);
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
    public List<Product> getTopPricedProductsByCategory(
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
    public ResponseEntity<List<Product>> batchCreateProducts(@RequestBody List<Product> products) {
        List<Product> savedProducts = transactionalService.batchCreateProducts(products);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducts);
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
    public ResponseEntity<Product> replaceProduct(
            @PathVariable String oldProductId,
            @RequestBody Product newProduct) {
        
        if (!productRepository.existsById(oldProductId)) {
            return ResponseEntity.notFound().build();
        }
        
        Product replacedProduct = transactionalService.replaceProduct(oldProductId, newProduct);
        return ResponseEntity.ok(replacedProduct);
    }
} 