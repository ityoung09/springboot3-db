package com.kedaya.springboot3mongodb.util;

import com.kedaya.springboot3mongodb.constant.MongoConstant;
import com.mongodb.client.model.IndexOptions;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB工具类
 */
@Component
@RequiredArgsConstructor
public class MongoUtil {

    private final MongoTemplate mongoTemplate;

    /**
     * 创建查询条件（默认排除已删除的文档）
     */
    public Query createQuery() {
        return Query.query(Criteria.where(MongoConstant.Field.DELETED).ne(true));
    }

    /**
     * 创建更新对象（自动更新更新时间）
     */
    public Update createUpdate() {
        Update update = new Update();
        update.set(MongoConstant.Field.UPDATE_TIME, LocalDateTime.now());
        return update;
    }

    /**
     * 分页查询
     */
    public <T> Page<T> findPage(Query query, Pageable pageable, Class<T> entityClass, String collectionName) {
        // 添加默认条件（排除已删除）
        query.addCriteria(Criteria.where(MongoConstant.Field.DELETED).ne(true));
        
        // 获取总数
        long total = mongoTemplate.count(query, entityClass, collectionName);
        
        // 添加分页条件
        query.with(pageable);
        
        // 执行查询
        List<T> content = mongoTemplate.find(query, entityClass, collectionName);
        
        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 逻辑删除
     */
    public long logicDelete(Query query, String collectionName) {
        Update update = createUpdate();
        update.set(MongoConstant.Field.DELETED, true);
        return mongoTemplate.updateMulti(query, update, collectionName).getModifiedCount();
    }
    
    /**
     * 创建索引
     */
    public void createIndex(String collectionName, String field, boolean unique) {
        IndexOptions indexOptions = new IndexOptions().unique(unique);
        mongoTemplate.getCollection(collectionName)
                .createIndex(new Document(field, 1), indexOptions);
    }
} 