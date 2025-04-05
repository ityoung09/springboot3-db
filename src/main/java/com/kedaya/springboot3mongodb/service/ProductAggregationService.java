package com.kedaya.springboot3mongodb.service;

import com.kedaya.springboot3mongodb.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ProductAggregationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 按类别分组并计算每个类别的平均价格
     * @return 类别及其平均价格的列表
     */
    public List<Map> getAveragePriceByCategory() {
        // 1. 定义分组操作
        GroupOperation groupByCategory = Aggregation.group("category")
                .avg("price").as("averagePrice")
                .count().as("productCount");
        
        // 2. 定义排序操作
        SortOperation sortByAvgPrice = Aggregation.sort(Sort.Direction.DESC, "averagePrice");
        
        // 3. 指定要包含的字段
        ProjectionOperation projectFields = Aggregation.project()
                .and("_id").as("category")
                .and("averagePrice").as("averagePrice")
                .and("productCount").as("productCount");
        
        // 4. 创建聚合管道
        Aggregation aggregation = Aggregation.newAggregation(
                groupByCategory,
                sortByAvgPrice,
                projectFields
        );
        
        // 5. 执行聚合操作
        AggregationResults<Map> results = mongoTemplate.aggregate(
                aggregation, "products", Map.class);
        
        return results.getMappedResults();
    }
    
    /**
     * 查找特定类别中价格最高的N个产品
     * @param category 产品类别
     * @param limit 返回结果数量
     * @return 产品列表
     */
    public List<Product> getTopPricedProductsByCategory(String category, int limit) {
        // 1. 定义匹配操作
        MatchOperation matchCategory = Aggregation.match(
                Criteria.where("category").is(category));
        
        // 2. 定义排序操作
        SortOperation sortByPrice = Aggregation.sort(Sort.Direction.DESC, "price");
        
        // 3. 定义限制结果数量操作
        LimitOperation limitResults = Aggregation.limit(limit);
        
        // 4. 创建聚合管道
        Aggregation aggregation = Aggregation.newAggregation(
                matchCategory,
                sortByPrice,
                limitResults
        );
        
        // 5. 执行聚合操作
        AggregationResults<Product> results = mongoTemplate.aggregate(
                aggregation, "products", Product.class);
        
        return results.getMappedResults();
    }
    
    /**
     * 按价格范围分组统计产品数量
     * @return 价格范围及对应的产品数量
     */
    public List<Map> countProductsByPriceRange() {
        // 定义价格范围条件
        ProjectionOperation projectFields = Aggregation.project("price")
                .and(ConditionalOperators.when(Criteria.where("price").lt(new BigDecimal(100)))
                        .then("便宜")
                        .otherwise(ConditionalOperators.when(Criteria.where("price").lt(new BigDecimal(500)))
                                .then("适中")
                                .otherwise("昂贵")))
                .as("priceRange");
        
        // 按价格范围分组并计数
        GroupOperation groupByPriceRange = Aggregation.group("priceRange")
                .count().as("count");
        
        // 创建聚合管道
        Aggregation aggregation = Aggregation.newAggregation(
                projectFields,
                groupByPriceRange
        );
        
        // 执行聚合操作
        AggregationResults<Map> results = mongoTemplate.aggregate(
                aggregation, "products", Map.class);
        
        return results.getMappedResults();
    }
} 