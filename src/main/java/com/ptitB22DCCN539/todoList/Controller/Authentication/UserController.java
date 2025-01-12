package com.ptitB22DCCN539.todoList.Controller.Authentication;

import com.ptitB22DCCN539.todoList.Bean.ContantVariable;
import com.ptitB22DCCN539.todoList.Bean.SuccessVariable;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserChangePasswordRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserUpdateRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.APIResponse;
import com.ptitB22DCCN539.todoList.Modal.Response.UserResponse;
import com.ptitB22DCCN539.todoList.Service.Authentication.User.IUserServiceAuthentication;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController(value = "UserAuthController")
@RequestMapping(value = "/${apiPrefix}/auth/users")
public class UserController {
    private final IUserServiceAuthentication userService;
    @Value(value = "${domainBackend}")
    private String domainBackend;

    @Autowired
    public UserController(IUserServiceAuthentication userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/my-info")
    public ResponseEntity<APIResponse> getMyInfo() {
        UserResponse userResponse = userService.getMyInfo();
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.QUERY_SUCCESS)
                .response(userResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/change-password")
    public ResponseEntity<APIResponse> changePassword(@Valid @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        UserResponse userResponse = userService.changePassword(userChangePasswordRequest);
        ResponseCookie cookie = ResponseCookie.from(ContantVariable.TOKEN_NAME, "")
                .maxAge(Duration.ZERO)
                .domain(domainBackend)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .build();
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.UPDATE_SUCCESS)
                .response(userResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(response);
    }

    @PutMapping(value = "/")
    public ResponseEntity<APIResponse> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        UserResponse userResponse = userService.updateUser(userUpdateRequest);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.UPDATE_SUCCESS)
                .response(userResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
