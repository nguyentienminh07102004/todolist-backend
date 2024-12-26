package com.ptitB22DCCN539.todoList.Modal.Request.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForgotPasswordRequest {
    private String code;
    private String password;
    private String rePassword;
}
