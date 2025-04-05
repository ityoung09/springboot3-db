package com.kedaya.springboot3mongodb.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户数据传输对象
 */
@Data
public class UserDTO {
    
    private String id;
    
    private String username;
    
    private String password;
    
    private String email;
    
    private String nickname;
    
    private Integer age;
    
    private String avatar;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 