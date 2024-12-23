package com.ptitB22DCCN539.todoList.Mapper.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ptitB22DCCN539.todoList.Modal.Entity.UserEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserRegisterRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse entityToResponse(UserEntity userEntity);
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserEntity registerRequestToEntity(UserRegisterRequest userRegisterRequest);
}