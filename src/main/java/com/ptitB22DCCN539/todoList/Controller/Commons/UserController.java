package com.ptitB22DCCN539.todoList.Controller.Commons;

import com.ptitB22DCCN539.todoList.Modal.Request.User.LoginRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.LoginWithGoogleRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserRegisterRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.APIResponse;
import com.ptitB22DCCN539.todoList.Modal.Response.UserResponse;
import com.ptitB22DCCN539.todoList.Service.Commons.User.IUserServiceCommons;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/${apiPrefix}/users")
public class UserController {
    private final IUserServiceCommons userService;

    @PostMapping(value = "/login")
    public ResponseEntity<APIResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = userService.login(loginRequest);
        APIResponse response = APIResponse.builder()
                .message("LOGIN SUCCESS")
                .response(token)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<APIResponse> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        UserResponse userResponse = userService.register(userRegisterRequest);
        APIResponse response = APIResponse.builder()
                .message("REGISTER SUCCESS")
                .response(userResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/login/google")
    public ResponseEntity<APIResponse> loginWithGoogle(@RequestBody LoginWithGoogleRequest loginWithGoogleRequest) {
        String token = userService.loginWithGoogle(loginWithGoogleRequest.getCode());
        APIResponse response = APIResponse.builder()
                .message("LOGIN SUCCESS")
                .response(token)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/forgot-password/{email}")
    public ResponseEntity<APIResponse> forgotPassword(@PathVariable String email) {
        userService.forgotPassword(email);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
