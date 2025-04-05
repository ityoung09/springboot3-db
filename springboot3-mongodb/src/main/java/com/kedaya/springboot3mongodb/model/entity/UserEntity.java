package com.kedaya.springboot3mongodb.model.entity;

import com.kedaya.springboot3mongodb.constant.MongoConstant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@Document(collection = MongoConstant.Collection.USER)
public class UserEntity {

    /**
     * 用户主键
     */
    @Id
    private String id;

    /**
     * 用户名
     */
    @Indexed(unique = true)
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户邮箱
     */
    @Indexed
    private String email;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 创建时间
     */
    @Field(MongoConstant.Field.CREATE_TIME)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Field(MongoConstant.Field.UPDATE_TIME)
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @Field(MongoConstant.Field.DELETED)
    private Boolean deleted;
} 