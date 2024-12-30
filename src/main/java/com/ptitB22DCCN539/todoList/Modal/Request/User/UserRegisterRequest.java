package com.ptitB22DCCN539.todoList.Modal.Request.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    @Email
    private String email;
    private String fullName;
    private String phone;
    @Size(min = 8)
    private String password;
    @Size(min = 8)
    private String rePassword;
    private List<String> roles;
}
