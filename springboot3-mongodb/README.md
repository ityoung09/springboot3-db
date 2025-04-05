# Spring Boot 3 MongoDB 集成示例

本模块展示了Spring Boot 3与MongoDB的集成方式以及MongoDB的高级特性使用。

## 功能特性

### 1. 基础CRUD操作
- 通过Spring Data MongoDB实现基础的增删改查
- 支持自定义查询方法和MongoDB原生查询

### 2. 索引优化
- 单字段索引（@Indexed）
- 唯一索引（@Indexed(unique = true)）
- 复合索引（@CompoundIndex）
- 后台创建索引（background = true）

### 3. 聚合管道
- 分组统计（按类别计算平均价格）
- 条件查询（查找特定类别中最贵的产品）
- 范围分组（按价格范围统计产品数量）

### 4. 事务支持
- 注解式事务（@Transactional）
- 编程式事务（TransactionTemplate）
- 事务回滚演示

### 5. GridFS文件存储
- 大文件上传与下载
- 元数据管理
- 按文件ID或文件名检索

## 如何运行

### 前置条件
- JDK 17+
- MongoDB 4.4+（事务功能需要副本集模式）
- Maven 3.8+

### 启动MongoDB（副本集模式）
```bash
# 启动单节点副本集（用于测试）
mongod --replSet rs0 --dbpath /data/db

# 初始化副本集
mongo
> rs.initiate()
```

### 运行应用
```bash
# 构建并运行
mvn spring-boot:run
```

## API接口

### 产品管理
- `GET /products` - 获取所有产品
- `GET /products/{id}` - 获取单个产品
- `POST /products` - 创建产品
- `PUT /products/{id}` - 更新产品
- `DELETE /products/{id}` - 删除产品

### 聚合查询
- `GET /products/aggregation/avgPriceByCategory` - 按类别获取平均价格
- `GET /products/aggregation/topPriced/{category}/{limit}` - 获取类别中最贵的N个产品
- `GET /products/aggregation/countByPriceRange` - 按价格范围分组统计

### 事务操作
- `POST /products/batch` - 批量创建产品（事务）
- `PUT /products/batch/price/{amount}` - 批量更新价格（事务）
- `PUT /products/replace/{oldProductId}` - 替换产品（事务）

### 文件管理
- `POST /files/upload` - 上传文件
- `GET /files/download/{fileId}` - 下载文件（按ID）
- `GET /files/download/byName/{filename}` - 下载文件（按文件名）
- `DELETE /files/{fileId}` - 删除文件

## 配置说明

应用配置位于`src/main/resources/application.properties`，主要配置项：

```properties
# MongoDB连接配置
spring.data.mongodb.uri=mongodb://localhost:27017/productdb
spring.data.mongodb.auto-index-creation=true

# 文件上传配置
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# 日志级别
logging.level.org.springframework.data.mongodb.core.MongoTemplate=DEBUG
```

## 测试用例

项目包含完整的单元测试，展示了如何测试MongoDB的各项功能：

- 索引和基本查询测试
- 聚合查询测试
- 文件上传下载测试

## 详细文档

更多详细说明请参考[MongoDB高级特性使用指南](./docs/MongoDB高级特性使用指南.md) 