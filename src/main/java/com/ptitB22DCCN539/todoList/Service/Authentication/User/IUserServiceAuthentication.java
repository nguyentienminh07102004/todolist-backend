package com.ptitB22DCCN539.todoList.Service.Authentication.User;

import com.ptitB22DCCN539.todoList.Modal.Entity.UserEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserChangePasswordRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.UserResponse;

public interface IUserServiceAuthentication {
    UserResponse changePassword(UserChangePasswordRequest userChangePasswordRequest);
    UserEntity getUserEntityById(String email);
}
