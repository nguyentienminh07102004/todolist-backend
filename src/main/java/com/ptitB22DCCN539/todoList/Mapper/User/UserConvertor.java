package com.ptitB22DCCN539.todoList.Mapper.User;

import com.ptitB22DCCN539.todoList.CustomerException.DataInvalidException;
import com.ptitB22DCCN539.todoList.Modal.Entity.RoleEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.UserEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserRegisterRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.UserResponse;
import com.ptitB22DCCN539.todoList.Repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserConvertor {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;

    public UserResponse entityToResponse(UserEntity user) {
        return userMapper.entityToResponse(user);
    }

    public UserEntity registerRequestToEntity(UserRegisterRequest userRegisterRequest) {
        UserEntity user = userMapper.registerRequestToEntity(userRegisterRequest);
        // set password
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        // set role
        if (userRegisterRequest.getRoles() != null) {
            List<RoleEntity> roles = userRegisterRequest.getRoles().stream()
                    .map(code -> roleRepository.findById(code)
                            .orElseThrow(() -> new DataInvalidException("Role not found!")))
                    .toList();
            user.setRoles(roles);
        } else {
            RoleEntity roleEntity = roleRepository.findById("USER")
                .orElseThrow(() -> new DataInvalidException("Role User not exists"));
            user.setRoles(List.of(roleEntity));
        }
        // set status
        user.setStatus("ACTIVE");
        return user;
    }
}
