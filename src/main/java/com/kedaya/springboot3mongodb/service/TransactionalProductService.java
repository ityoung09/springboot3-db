package com.kedaya.springboot3mongodb.service;

import com.kedaya.springboot3mongodb.model.Product;
import com.kedaya.springboot3mongodb.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionalProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private MongoTransactionManager transactionManager;

    /**
     * 使用注解方式的事务
     * 批量添加产品，如果有任何一个失败，则全部回滚
     */
    @Transactional
    public List<Product> batchCreateProducts(List<Product> products) {
        // 在这个方法中的所有数据库操作都将在一个事务中
        return productRepository.saveAll(products);
    }
    
    /**
     * 使用编程式事务
     * 批量更新产品价格
     */
    public void updateProductPrices(List<String> productIds, BigDecimal increaseAmount) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        
        transactionTemplate.execute(status -> {
            try {
                for (String id : productIds) {
                    Product product = productRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("产品未找到：" + id));
                    
                    BigDecimal newPrice = product.getPrice().add(increaseAmount);
                    product.setPrice(newPrice);
                    
                    productRepository.save(product);
                }
                return null;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }
    
    /**
     * 组合操作：创建产品并删除过期产品
     */
    @Transactional
    public Product replaceProduct(String oldProductId, Product newProduct) {
        // 删除旧产品
        productRepository.deleteById(oldProductId);
        
        // 创建新产品
        return productRepository.save(newProduct);
        
        // 如果任一操作失败，两个操作都会回滚
    }
} 