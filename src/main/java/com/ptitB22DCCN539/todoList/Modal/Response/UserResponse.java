package com.ptitB22DCCN539.todoList.Modal.Response;

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
public class UserResponse {
    private String fullName;
    private String email;
    private String phone;
    private List<RoleResponse> roles;
    private String status;
}
