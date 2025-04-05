package com.kedaya.springboot3mongodb.service;

import com.kedaya.springboot3mongodb.model.entity.ProductEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
interface TransactionalProductService {

    /**
     * 使用注解方式的事务
     * 批量添加产品，如果有任何一个失败，则全部回滚
     */
    List<ProductEntity> batchCreateProducts(List<ProductEntity> productEntities);

    /**
     * 使用编程式事务
     * 批量更新产品价格
     */
    void updateProductPrices(List<String> productIds, BigDecimal increaseAmount);

    /**
     * 组合操作：创建产品并删除过期产品
     */
    ProductEntity replaceProduct(String oldProductId, ProductEntity newProductEntity);
} 