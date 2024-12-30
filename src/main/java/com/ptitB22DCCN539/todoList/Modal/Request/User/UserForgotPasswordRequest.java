package com.ptitB22DCCN539.todoList.Modal.Request.User;

import jakarta.validation.constraints.Size;
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
    @Size(min = 8)
    private String password;
    @Size(min = 8)
    private String rePassword;
}
