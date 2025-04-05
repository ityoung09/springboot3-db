package com.kedaya.springboot3mongodb.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户视图对象
 */
@Data
public class UserVO {
    
    private String id;
    
    private String username;
    
    private String email;
    
    private String nickname;
    
    private Integer age;
    
    private String avatar;
    
    private LocalDateTime createTime;
} 