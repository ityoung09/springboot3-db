package com.kedaya.springboot3mongodb.repository;

import com.kedaya.springboot3mongodb.model.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户存储库接口
 */
@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    /**
     * 根据用户名查找用户
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * 根据年龄范围查找用户
     */
    List<UserEntity> findByAgeBetween(Integer minAge, Integer maxAge);

    /**
     * 查找未删除的用户
     */
    @Query("{'deleted': false}")
    List<UserEntity> findAllNotDeleted();
} 