# MongoDB高级特性使用指南

本指南介绍如何使用Spring Boot 3项目中添加的MongoDB高级特性，包括索引优化、聚合管道、事务支持和GridFS文件存储。

## 1. 项目配置

已在`application.properties`中配置了MongoDB连接：

```properties
spring.data.mongodb.host=156.238.252.42
spring.data.mongodb.port=27017
spring.data.mongodb.database=springboot3-mongodb
spring.data.mongodb.username=mongo_G4Te3f
spring.data.mongodb.password=mongo_mstssG
```

## 2. 索引优化

在`Product`实体类中使用了以下索引：

- 单字段索引：`@Indexed`标注了`name`、`category`和`createTime`字段
- 唯一索引：`name`字段设置为唯一索引`@Indexed(unique = true)`
- 复合索引：使用`@CompoundIndexes`定义了类别+价格的复合索引
- 后台创建索引：`@Indexed(background = true)`

测试索引效果：
1. 添加大量产品数据
2. 使用以下接口进行查询测试：`/productEntities/category/{category}`
3. 通过MongoDB Compass查看查询执行计划，验证索引使用情况

## 3. 聚合管道

`ProductAggregationService`提供了三种聚合查询示例：

- 按类别分组计算平均价格：`/productEntities/aggregation/avgPriceByCategory`
- 查找特定类别中价格最高的产品：`/productEntities/aggregation/topPriced/{category}/{limit}`
- 按价格范围分组统计产品数量：`/productEntities/aggregation/countByPriceRange`

测试步骤：
1. 添加多个不同类别、不同价格的产品
2. 访问上述接口观察聚合结果

## 4. 事务支持

**重要：MongoDB事务需要部署为副本集环境**

本地测试副本集配置：
```bash
# 启动单节点副本集
mongod --replSet rs0 --dbpath /data/db

# 连接MongoDB并初始化副本集
mongo
> rs.initiate()
```

`TransactionalProductService`展示了两种事务使用方式：

- 基于注解的事务：`@Transactional`
- 编程式事务：`TransactionTemplate`

测试接口：
- 批量创建产品：`POST /productEntities/batch`
- 批量更新价格：`PUT /productEntities/batch/price/{amount}`
- 替换产品：`PUT /productEntities/replace/{oldProductId}`

## 5. GridFS文件存储

GridFS用于存储大文件（如图片、视频等）。

主要功能接口：
- 上传文件：`POST /files/upload`
- 下载文件（按ID）：`GET /files/download/{fileId}`
- 下载文件（按文件名）：`GET /files/download/byName/{filename}`
- 删除文件：`DELETE /files/{fileId}`

测试方法：
```bash
# 上传文件
curl -X POST -F "file=@/path/to/your/file.jpg" http://localhost:8080/files/upload

# 下载文件
curl -X GET http://localhost:8080/files/download/{fileId} --output downloaded_file.jpg
```

## 6. 访问管理界面

推荐使用MongoDB Compass工具来可视化管理MongoDB数据：
- 索引管理
- 数据查询
- 聚合管道构建
- 性能监控

## 7. 注意事项

1. 索引虽然提高查询性能，但会降低写入性能，且占用存储空间
2. 复杂的聚合管道可能需要优化，避免内存占用过高
3. MongoDB事务有一定限制，不同于关系型数据库的ACID事务
4. GridFS适合存储大于16MB的文件，小文件可考虑直接存储为Binary数据 