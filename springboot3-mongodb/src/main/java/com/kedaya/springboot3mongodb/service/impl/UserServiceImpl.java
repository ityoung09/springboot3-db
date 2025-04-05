package com.kedaya.springboot3mongodb.service.impl;

import com.kedaya.springboot3mongodb.exception.UserNotFoundException;
import com.kedaya.springboot3mongodb.model.dto.UserDTO;
import com.kedaya.springboot3mongodb.model.entity.UserEntity;
import com.kedaya.springboot3mongodb.model.mapper.UserMapper;
import com.kedaya.springboot3mongodb.model.vo.UserVO;
import com.kedaya.springboot3mongodb.repository.UserRepository;
import com.kedaya.springboot3mongodb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserVO createUser(UserDTO userDTO) {
        UserEntity userEntity = UserMapper.INSTANCE.toEntity(userDTO);
        userEntity.setCreateTime(LocalDateTime.now());
        userEntity.setUpdateTime(LocalDateTime.now());
        UserEntity savedEntity = userRepository.save(userEntity);
        return UserMapper.INSTANCE.toVo(savedEntity);
    }

    @Override
    public UserVO updateUser(String id, UserDTO userDTO) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("用户不存在: " + id));

        UserEntity userEntity = UserMapper.INSTANCE.toEntity(userDTO);
        userEntity.setId(id);
        userEntity.setCreateTime(existingUser.getCreateTime());
        userEntity.setUpdateTime(LocalDateTime.now());
        userEntity.setDeleted(existingUser.getDeleted());

        UserEntity updatedEntity = userRepository.save(userEntity);
        return UserMapper.INSTANCE.toVo(updatedEntity);
    }

    @Override
    public void deleteUser(String id) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("用户不存在: " + id));
        
        // 逻辑删除
        existingUser.setDeleted(true);
        existingUser.setUpdateTime(LocalDateTime.now());
        userRepository.save(existingUser);
    }

    @Override
    public UserVO getUserById(String id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("用户不存在: " + id));
        return UserMapper.INSTANCE.toVo(userEntity);
    }

    @Override
    public UserVO getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("用户名不存在: " + username));
        return UserMapper.INSTANCE.toVo(userEntity);
    }

    @Override
    public List<UserVO> getAllUsers() {
        List<UserEntity> allUsers = userRepository.findAllNotDeleted();
        return allUsers.stream()
                .map(UserMapper.INSTANCE::toVo)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserVO> getUsersByAgeRange(Integer minAge, Integer maxAge) {
        List<UserEntity> users = userRepository.findByAgeBetween(minAge, maxAge);
        return users.stream()
                .map(UserMapper.INSTANCE::toVo)
                .collect(Collectors.toList());
    }
} 