package com.ptitB22DCCN539.todoList.Service.Commons.User;

import com.ptitB22DCCN539.todoList.Modal.Entity.UserEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.User.LoginRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserChangePasswordRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserRegisterRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.UserResponse;

public interface IUserServiceCommons {
    String login(LoginRequest loginRequest);
    UserResponse register(UserRegisterRequest userRegisterRequest);
    String loginWithGoogle(String code);
    void forgotPassword(String email);
}
