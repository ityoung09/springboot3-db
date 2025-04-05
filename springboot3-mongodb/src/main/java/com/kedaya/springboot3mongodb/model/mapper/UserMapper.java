package com.kedaya.springboot3mongodb.model.mapper;

import com.kedaya.springboot3mongodb.model.dto.UserDTO;
import com.kedaya.springboot3mongodb.model.entity.UserEntity;
import com.kedaya.springboot3mongodb.model.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 用户对象映射接口
 */
@Mapper
public interface UserMapper {
    
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    
    /**
     * DTO转Entity
     */
    @Mapping(target = "deleted", constant = "false")
    UserEntity toEntity(UserDTO dto);
    
    /**
     * Entity转DTO
     */
    UserDTO toDto(UserEntity entity);
    
    /**
     * Entity转VO
     */
    UserVO toVo(UserEntity entity);
} 