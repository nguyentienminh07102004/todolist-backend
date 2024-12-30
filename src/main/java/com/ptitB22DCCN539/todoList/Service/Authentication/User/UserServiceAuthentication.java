package com.ptitB22DCCN539.todoList.Service.Authentication.User;

import com.ptitB22DCCN539.todoList.CustomerException.DataInvalidException;
import com.ptitB22DCCN539.todoList.CustomerException.ExceptionVariable;
import com.ptitB22DCCN539.todoList.Mapper.User.UserConvertor;
import com.ptitB22DCCN539.todoList.Modal.Entity.JwtTokenEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.UserEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserChangePasswordRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.UserResponse;
import com.ptitB22DCCN539.todoList.Repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceAuthentication implements IUserServiceAuthentication {
    private final IUserRepository userRepository;
    private final UserConvertor userConvertor;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse changePassword(UserChangePasswordRequest userChangePasswordRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = this.getUserEntityById(email);
        if(!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new DataInvalidException(ExceptionVariable.OLD_PASSWORD_NOT_MATCH);
        }
        if(passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new DataInvalidException(ExceptionVariable.OLD_PASSWORD_NEW_PASSWORD_MATCH);
        }
        if(!userChangePasswordRequest.getNewPassword().equals(userChangePasswordRequest.getReNewPassword())) {
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
}
