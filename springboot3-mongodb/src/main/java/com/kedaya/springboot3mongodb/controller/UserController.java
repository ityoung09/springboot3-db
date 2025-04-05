package com.kedaya.springboot3mongodb.controller;

import com.kedaya.springboot3mongodb.model.dto.UserDTO;
import com.kedaya.springboot3mongodb.model.vo.UserVO;
import com.kedaya.springboot3mongodb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 创建用户
     */
    @PostMapping
    public ResponseEntity<UserVO> createUser(@RequestBody UserDTO userDTO) {
        UserVO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserVO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        UserVO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取单个用户
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserVO> getUserById(@PathVariable String id) {
        UserVO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 根据用户名查询用户
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserVO> getUserByUsername(@PathVariable String username) {
        UserVO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * 获取所有用户
     */
    @GetMapping
    public ResponseEntity<List<UserVO>> getAllUsers() {
        List<UserVO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 根据年龄范围查询用户
     */
    @GetMapping("/age")
    public ResponseEntity<List<UserVO>> getUsersByAgeRange(
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge) {
        List<UserVO> users = userService.getUsersByAgeRange(minAge, maxAge);
        return ResponseEntity.ok(users);
    }
} 