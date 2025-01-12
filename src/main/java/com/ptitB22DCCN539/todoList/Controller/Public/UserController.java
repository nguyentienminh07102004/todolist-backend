package com.ptitB22DCCN539.todoList.Controller.Public;

import com.ptitB22DCCN539.todoList.Bean.ContantVariable;
import com.ptitB22DCCN539.todoList.Bean.SuccessVariable;
import com.ptitB22DCCN539.todoList.Modal.Entity.JwtTokenEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.User.LoginRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.LoginWithGoogleRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserForgotPasswordRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserRegisterRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.APIResponse;
import com.ptitB22DCCN539.todoList.Modal.Response.UserResponse;
import com.ptitB22DCCN539.todoList.Service.Public.User.IUserServicePublic;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/${apiPrefix}/users")
@Slf4j
public class UserController {
    private final IUserServicePublic userService;

    @PostMapping(value = "/login")
    public ResponseEntity<APIResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtTokenEntity token = userService.login(loginRequest);
        // set cookie
        ResponseCookie cookie = ResponseCookie.from(ContantVariable.TOKEN_NAME, token.getToken())
                .httpOnly(true)
                .path("/")
                .domain("todolist-frontend-iota.vercel.app")
                .maxAge(Duration.between(LocalDateTime.now(), token.getExpired()))
                .build();
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.LOGIN_SUCCESS)
                .response(token)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<APIResponse> register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        UserResponse userResponse = userService.register(userRegisterRequest);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message(SuccessVariable.REGISTER_SUCCESS)
                .response(userResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/login/google")
    public ResponseEntity<APIResponse> loginWithGoogle(@Valid @RequestBody LoginWithGoogleRequest loginWithGoogleRequest) {
        JwtTokenEntity token = userService.loginWithGoogle(loginWithGoogleRequest.getCode());
        ResponseCookie cookie = ResponseCookie.from(ContantVariable.TOKEN_NAME, token.getToken())
                .httpOnly(true)
                .maxAge(Duration.between(LocalDateTime.now(), token.getExpired()))
                .domain("todolist-frontend-iota.vercel.app")
                .path("/")
                .build();
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.LOGIN_SUCCESS)
                .response(token)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, String.valueOf(cookie))
                .body(response);
    }

    @PutMapping(value = "/forgot-password/{email}")
    public ResponseEntity<APIResponse> forgotPassword(@PathVariable String email) {
        userService.forgotPassword(email);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.SUCCESS)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/verify-code-set-password")
    public ResponseEntity<APIResponse> verifyCode(@Valid @RequestBody UserForgotPasswordRequest userForgotPasswordRequest) {
        UserResponse userResponse = userService.verifyCodeAndSetPassword(userForgotPasswordRequest);
        ResponseCookie cookie = ResponseCookie.from(ContantVariable.TOKEN_NAME, "")
                .httpOnly(true)
                .path("/")
                .domain("todolist-frontend-iota.vercel.app")
                .maxAge(Duration.ZERO)
                .build();
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.CHANGE_PASSWORD_SUCCESS)
                .response(userResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, String.valueOf(cookie))
                .body(response);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<APIResponse> logout(HttpServletRequest httpServletRequest) {
        userService.logout(httpServletRequest);
        // delete token
        ResponseCookie cookie = ResponseCookie.from(ContantVariable.TOKEN_NAME, "")
                .maxAge(Duration.ZERO)
                .httpOnly(true)
                .path("/")
                .domain("todolist-frontend-iota.vercel.app")
                .build();
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.LOGOUT_SUCCESS)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, String.valueOf(cookie))
                .body(response);
    }
}