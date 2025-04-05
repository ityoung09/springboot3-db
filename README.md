# Spring Boot 3 数据库集成示例

本项目集合了Spring Boot 3与各种常用数据库的集成示例，旨在提供完整的配置与使用参考。每个子模块都是一个独立可运行的Spring Boot应用，展示了特定数据库的集成方式和高级特性。

## 项目结构

```
springboot3-db/
├── springboot3-mongodb/    # MongoDB集成示例
├── 其他数据库模块将陆续添加...
```

## 已包含的数据库集成

### MongoDB (springboot3-mongodb)

MongoDB集成示例，展示了以下功能与特性：

- 基础CRUD操作
- 索引优化（单字段索引、复合索引、唯一索引等）
- 聚合管道查询（分组、排序、过滤等复杂查询）
- 事务支持（注解式事务和编程式事务）
- GridFS大文件存储（上传、下载、删除）

详情请参考 [MongoDB高级特性使用指南](./springboot3-mongodb/docs/MongoDB高级特性使用指南.md)

## 如何使用

### 环境要求

- JDK 17+
- Maven 3.8+
- 相应的数据库服务（如MongoDB、MySQL等）

### 运行项目

每个模块都可以独立运行：

```bash
# 进入相应模块目录
cd springboot3-mongodb

# 使用Maven构建并运行
mvn spring-boot:run
```

### 配置说明

各个模块的配置文件位于 `src/main/resources/application.properties` 或 `application.yml`，可根据自己的环境进行调整。

## 特性说明

### MongoDB模块

- **端口**: 8080
- **API文档**: 启动后访问 http://localhost:8080/swagger-ui.html
- **测试类**: 包含完整的功能测试示例

## 注意事项

- 所有示例采用Spring Boot 3.x版本
- 使用了Jakarta EE 9+规范（替代了之前的JavaEE）
- 部分高级特性（如MongoDB事务）可能需要特定的数据库版本或配置支持

## 未来计划

计划添加以下数据库的集成示例：

- MySQL/MariaDB
- PostgreSQL
- Redis
- Elasticsearch
- 其他NoSQL数据库

## 贡献指南

欢迎贡献代码或提出建议：

1. Fork本仓库
2. 创建自己的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交你的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建一个Pull Request

## 许可证

[MIT许可证](LICENSE) 