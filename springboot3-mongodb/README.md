# SpringBoot 3 MongoDB 集成项目

这是一个基于Spring Boot 3和MongoDB的企业级项目骨架，提供了完整的CRUD操作和标准化的项目结构。

## 项目结构

```
com.kedaya.springboot3mongodb
├── config                          # 配置类目录
│   └── MongoConfig.java            # MongoDB配置类
├── constant                        # 常量类目录
│   └── MongoConstant.java          # MongoDB相关常量
├── controller                      # 控制层
│   └── api                         # API接口目录
│       └── v1                      # 版本号
│           └── UserController.java # 控制器类
├── model                           # 模型层
│   ├── dto                         # 数据传输对象
│   │   └── UserDTO.java            # DTO类
│   ├── entity                      # 实体类(文档模型)
│   │   └── UserEntity.java         # 实体类
│   ├── vo                          # 视图对象
│   │   └── UserVO.java             # VO类
│   └── mapper                      # 对象映射转换器
│       └── UserMapper.java         # 映射类
├── repository                      # MongoDB存储库接口层
│   └── UserRepository.java         # 存储库接口
├── service                         # 服务层
│   ├── UserService.java            # 服务接口
│   └── impl                        # 服务实现
│       └── UserServiceImpl.java    # 服务实现类
├── util                            # 工具类目录
│   └── MongoUtil.java              # MongoDB工具类
├── exception                       # 异常处理目录
│   ├── GlobalExceptionHandler.java # 全局异常处理器
│   └── UserNotFoundException.java  # 用户不存在异常
└── Springboot3MongodbApplication.java # 主应用类
```

## 技术栈

- Spring Boot 3.4.4
- Spring Data MongoDB
- Lombok
- MapStruct
- Docker

## 功能特性

- 用户CRUD操作
- 全局异常处理
- MongoDB查询和更新工具类
- 分层架构设计
- Docker容器化支持

## 快速开始

### 使用Docker启动MongoDB（开发环境）

```bash
docker-compose up -d
```

### 构建并运行项目

```bash
./mvnw clean package
java -jar target/springboot3-mongodb-0.0.1-SNAPSHOT.jar
```

## API接口

### 用户管理

| 方法   | URL                           | 描述               |
|------|-------------------------------|------------------|
| POST | /api/v1/users                 | 创建用户             |
| PUT  | /api/v1/users/{id}            | 更新用户             |
| DELETE | /api/v1/users/{id}          | 删除用户             |
| GET  | /api/v1/users/{id}            | 获取单个用户           |
| GET  | /api/v1/users/username/{username} | 根据用户名查询用户 |
| GET  | /api/v1/users                 | 获取所有用户           |
| GET  | /api/v1/users/age?minAge=18&maxAge=60 | 根据年龄范围查询用户 |

## 配置多环境

项目包含开发环境和生产环境的配置文件：

- `application.properties`: 通用配置
- `application-dev.properties`: 开发环境配置
- `application-prod.properties`: 生产环境配置

## 注意事项

- 默认激活开发环境配置
- 生产环境部署时，使用以下命令指定环境：

```bash
java -jar -Dspring.profiles.active=prod target/springboot3-mongodb-0.0.1-SNAPSHOT.jar
``` 