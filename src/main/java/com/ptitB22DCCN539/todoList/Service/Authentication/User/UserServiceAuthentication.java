package com.ptitB22DCCN539.todoList.Service.Authentication.User;

import com.ptitB22DCCN539.todoList.CustomerException.DataInvalidException;
import com.ptitB22DCCN539.todoList.CustomerException.ExceptionVariable;
import com.ptitB22DCCN539.todoList.Mapper.User.UserConvertor;
import com.ptitB22DCCN539.todoList.Modal.Entity.JwtTokenEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.UserEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserChangePasswordRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserUpdateRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.UserResponse;
import com.ptitB22DCCN539.todoList.Repository.IRoleRepository;
import com.ptitB22DCCN539.todoList.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceAuthentication implements IUserServiceAuthentication {
    private final IUserRepository userRepository;
    private final UserConvertor userConvertor;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;

    @Autowired
    public UserServiceAuthentication(IUserRepository userRepository,
                                     UserConvertor userConvertor,
                                     PasswordEncoder passwordEncoder,
                                     IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userConvertor = userConvertor;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserResponse changePassword(UserChangePasswordRequest userChangePasswordRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = this.getUserEntityById(email);
        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new DataInvalidException(ExceptionVariable.OLD_PASSWORD_NOT_MATCH);
        }
        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new DataInvalidException(ExceptionVariable.OLD_PASSWORD_NEW_PASSWORD_MATCH);
        }
        if (!userChangePasswordRequest.getNewPassword().equals(userChangePasswordRequest.getReNewPassword())) {
            throw new DataInvalidException(ExceptionVariable.PASSWORD_AND_REPEAT_PASSWORD_NOT_MATCH);
        }
        for (JwtTokenEntity jwtTokenEntity : user.getJwtTokens()) {
            jwtTokenEntity.setUser(null);
        }
        user.getJwtTokens().clear();
        user.setPassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return userConvertor.entityToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserEntityById(String email) {
        return userRepository.findById(email)
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.EMAIL_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getMyInfo() {
        return userConvertor.entityToResponse(this.getUserEntityById(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @Override
    @Transactional
    @PreAuthorize(value = "#userUpdateRequest.email.equals(authentication.name)")
    public UserResponse updateUser(UserUpdateRequest userUpdateRequest) {
        UserEntity user = this.getUserEntityById(userUpdateRequest.getEmail());
        user.setPhone(userUpdateRequest.getPhone());
        user.setFullName(userUpdateRequest.getFullName());
        if (userUpdateRequest.getRoles() != null) {
            user.setRoles(userUpdateRequest.getRoles().stream()
                    .map(id -> roleRepository.findById(id)
                            .orElseThrow(() -> new DataInvalidException(ExceptionVariable.ROLE_NOT_FOUND)))
                    .toList());
        }
        userRepository.save(user);
        return userConvertor.entityToResponse(user);
    }
}
