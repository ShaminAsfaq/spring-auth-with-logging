package com.example.auth.model.mapper;

import com.example.auth.model.dto.UserDTO;
import com.example.auth.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    UserDTO fromEntity(UserEntity userEntity);

    @Mapping(target = "password", ignore = true)
    UserEntity toEntity(UserDTO userDTO);
}
