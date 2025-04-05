package com.kedaya.springboot3mongodb.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.mongodb.MongoCredential.createCredential;
import static java.util.Collections.singletonList;

/**
 * MongoDB配置类
 */
@Configuration
public class MongoConfig {

    @Autowired
    private MongoProperties mongoProperties;

    /**
     * 配置验证器
     */
    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(LocalValidatorFactoryBean factory) {
        return new ValidatingMongoEventListener(factory);
    }

    /**
     * 配置自定义转换器
     */
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(new ArrayList<>());
    }

    /**
     * 配置MongoDB事务管理器
     *
     * @param dbFactory MongoDB数据库工厂
     * @return 事务管理器
     */
    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    /**
     * 创建MongoDB客户端，配置连接池信息
     * @return
     * @throws Exception
     */
    @Bean
    public MongoClient mongoClient() throws Exception {
        String mongoUri = buildMongoUri();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(mongoUri))
                .applyToClusterSettings(setting -> {
                    setting.hosts(singletonList(new ServerAddress(mongoProperties.getHost(), mongoProperties.getPort())));
                })
                // 配置连接池信息
                .applyToConnectionPoolSettings(builder -> builder
                        .maxSize(100)           // 最大连接数
                        .minSize(10)            // 最小连接数
                        .maxWaitTime(120000, java.util.concurrent.TimeUnit.MILLISECONDS) // 最大等待时间
                        .maxConnectionIdleTime(300000, java.util.concurrent.TimeUnit.MILLISECONDS) // 最大空闲时间
                        .maxConnectionLifeTime(0, java.util.concurrent.TimeUnit.MILLISECONDS) // 最大存活时间
                )
                .build();
        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, mongoProperties.getDatabase());
    }

    private String buildMongoUri() throws Exception {
        // 如果配置了 uri，直接使用
        if (mongoProperties.getUri() != null) {
            return mongoProperties.getUri();
        }

        // 否则手动构造
        String host = mongoProperties.getHost() != null ? mongoProperties.getHost() : "localhost";
        int port = mongoProperties.getPort() != 0 ? mongoProperties.getPort() : 27017;
        String database = mongoProperties.getDatabase();
        String username = mongoProperties.getUsername();
        String password = String.valueOf(mongoProperties.getPassword());
        String authDatabase = mongoProperties.getAuthenticationDatabase() != null
                ? mongoProperties.getAuthenticationDatabase()
                : database;

        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
            String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8.toString());
            return String.format("mongodb://%s:%s@%s:%d/%s?authSource=%s",
                    encodedUsername, encodedPassword, host, port, database, authDatabase);
        } else {
            return String.format("mongodb://%s:%d/%s", host, port, database);
        }
    }
} 