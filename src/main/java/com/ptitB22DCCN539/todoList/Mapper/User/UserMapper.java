package com.ptitB22DCCN539.todoList.Mapper.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ptitB22DCCN539.todoList.Modal.Entity.UserEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserRegisterRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.UserResponse;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse entityToResponse(UserEntity userEntity);

    @Mappings(value = {
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "jwtTokens", ignore = true),
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "password", ignore = true)})
    UserEntity registerRequestToEntity(UserRegisterRequest userRegisterRequest);
}