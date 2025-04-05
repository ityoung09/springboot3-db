package com.kedaya.springboot3mongodb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

/**
 * MongoDB事务配置类
 * 注意：要使用事务，MongoDB必须配置为副本集
 */
@Configuration
public class MongoTransactionConfig {

    /**
     * 配置MongoDB事务管理器
     * @param dbFactory MongoDB数据库工厂
     * @return 事务管理器
     */
    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
} 