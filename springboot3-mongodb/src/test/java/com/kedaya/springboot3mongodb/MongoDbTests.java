package com.kedaya.springboot3mongodb;

import com.kedaya.springboot3mongodb.model.entity.ProductEntity;
import com.kedaya.springboot3mongodb.repository.ProductRepository;
import com.kedaya.springboot3mongodb.service.impl.ProductAggregationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * MongoDB高级特性功能测试
 */
@SpringBootTest
public class MongoDbTests {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductAggregationServiceImpl aggregationService;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    /**
     * 测试创建产品和索引
     */
    @Test
    public void testCreateProducts() {
        // 清空现有数据
        productRepository.deleteAll();
        
        // 创建测试产品
        ProductEntity p1 = new ProductEntity();
        p1.setName("iPhone 14");
        p1.setPrice(new BigDecimal("5999"));
        p1.setCategory("手机");
        p1.setDescription("苹果手机");
        
        ProductEntity p2 = new ProductEntity();
        p2.setName("MacBook Pro");
        p2.setPrice(new BigDecimal("12999"));
        p2.setCategory("电脑");
        p2.setDescription("苹果笔记本电脑");
        
        ProductEntity p3 = new ProductEntity();
        p3.setName("小米12");
        p3.setPrice(new BigDecimal("3999"));
        p3.setCategory("手机");
        p3.setDescription("小米手机");
        
        // 保存产品
        productRepository.saveAll(Arrays.asList(p1, p2, p3));
        
        // 查询测试
        List<ProductEntity> phones = productRepository.findByCategory("手机");
        assertEquals(2, phones.size());
        
        List<ProductEntity> expensivePhones = productRepository.findByCategoryAndPriceGreaterThan("手机", new BigDecimal("4000"));
        assertEquals(1, expensivePhones.size());
        assertEquals("iPhone 14", expensivePhones.get(0).getName());
    }
    
    /**
     * 测试聚合查询
     */
    @Test
    public void testAggregationQueries() {
        // 确保有测试数据
        if (productRepository.count() == 0) {
            testCreateProducts();
        }
        
        // 测试按类别分组计算平均价格
        List<Map> avgPriceByCategory = aggregationService.getAveragePriceByCategory();
        assertNotNull(avgPriceByCategory);
        avgPriceByCategory.forEach(map -> {
            System.out.println("类别: " + map.get("category") + 
                    ", 平均价格: " + map.get("averagePrice") +
                    ", 产品数量: " + map.get("productCount"));
        });
        
        // 测试获取类别中最贵的产品
        List<ProductEntity> topPhones = aggregationService.getTopPricedProductsByCategory("手机", 1);
        assertEquals(1, topPhones.size());
        assertEquals("iPhone 14", topPhones.get(0).getName());
        
        // 测试按价格范围分组
        List<Map> countByPriceRange = aggregationService.countProductsByPriceRange();
        assertNotNull(countByPriceRange);
        countByPriceRange.forEach(map -> {
            System.out.println("价格范围: " + map.get("_id") + 
                    ", 产品数量: " + map.get("count"));
        });
    }
} 