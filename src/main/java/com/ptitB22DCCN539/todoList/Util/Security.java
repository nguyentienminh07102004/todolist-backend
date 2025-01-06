package com.ptitB22DCCN539.todoList.Util;

import com.ptitB22DCCN539.todoList.CustomerException.DataInvalidException;
import com.ptitB22DCCN539.todoList.CustomerException.ExceptionVariable;
import com.ptitB22DCCN539.todoList.Repository.ITaskRepository;
import com.ptitB22DCCN539.todoList.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "securityUtils")
public class Security {
    private final IUserRepository userRepository;
    private final ITaskRepository taskRepository;

    @Autowired
    public Security(IUserRepository userRepository, ITaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public boolean checkTaskIdInUser(String taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new DataInvalidException(ExceptionVariable.UNAUTHENTICATED);
        }
        userRepository.findById(authentication.getName())
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.EMAIL_NOT_FOUND));
        return taskRepository.existsByIdAndCreatedBy(taskId, authentication.getName());
    }

    public boolean checkTaskIdInUser(List<String> ids) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new DataInvalidException(ExceptionVariable.UNAUTHENTICATED);
        }
        userRepository.findById(authentication.getName())
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.EMAIL_NOT_FOUND));
        return taskRepository.existsByIdInAndCreatedBy(ids, authentication.getName());
    }
}
