package com.project.homeListener.login.user.util;

import com.project.homeListener.login.user.dto.request.LoginRequestDTO;
import com.project.homeListener.login.user.dto.request.RegisterRequestDTO;
import com.project.homeListener.login.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "newPassword", target = "password")
    User toEntity(RegisterRequestDTO registerRequestDTO, String newPassword);
}
