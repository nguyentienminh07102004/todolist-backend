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
public class UserChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String reNewPassword;
}
