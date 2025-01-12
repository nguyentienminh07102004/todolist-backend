package com.ptitB22DCCN539.todoList.Modal.Request.User;

import jakarta.validation.constraints.Email;
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
public class UserUpdateRequest {
    @Email
    private String email;
    private String fullName;
    private String phone;
    private List<String> roles;
}
