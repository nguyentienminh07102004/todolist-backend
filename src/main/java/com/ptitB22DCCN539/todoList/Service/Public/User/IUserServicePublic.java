package com.ptitB22DCCN539.todoList.Service.Public.User;

import com.ptitB22DCCN539.todoList.Modal.Entity.JwtTokenEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.User.LoginRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserForgotPasswordRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserRegisterRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IUserServicePublic {
    JwtTokenEntity login(LoginRequest loginRequest);
    UserResponse register(UserRegisterRequest userRegisterRequest);
    JwtTokenEntity loginWithGoogle(String code);
    void forgotPassword(String email);
    UserResponse verifyCodeAndSetPassword(UserForgotPasswordRequest userForgotPasswordRequest);
    void logout(HttpServletRequest httpServletRequest);
}
