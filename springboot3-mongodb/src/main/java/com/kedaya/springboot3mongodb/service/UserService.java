package com.kedaya.springboot3mongodb.service;

import com.kedaya.springboot3mongodb.model.dto.UserDTO;
import com.kedaya.springboot3mongodb.model.vo.UserVO;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 创建用户
     */
    UserVO createUser(UserDTO userDTO);

    /**
     * 更新用户
     */
    UserVO updateUser(String id, UserDTO userDTO);

    /**
     * 删除用户
     */
    void deleteUser(String id);

    /**
     * 根据ID查询用户
     */
    UserVO getUserById(String id);

    /**
     * 根据用户名查询用户
     */
    UserVO getUserByUsername(String username);

    /**
     * 查询所有用户
     */
    List<UserVO> getAllUsers();

    /**
     * 根据年龄范围查询用户
     */
    List<UserVO> getUsersByAgeRange(Integer minAge, Integer maxAge);
} 