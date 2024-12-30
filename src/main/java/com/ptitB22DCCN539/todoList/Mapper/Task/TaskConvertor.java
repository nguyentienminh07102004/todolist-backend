package com.ptitB22DCCN539.todoList.Mapper.Task;

import com.ptitB22DCCN539.todoList.CustomerException.DataInvalidException;
import com.ptitB22DCCN539.todoList.CustomerException.ExceptionVariable;
import com.ptitB22DCCN539.todoList.Mapper.User.UserConvertor;
import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.UserEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.TaskResponse;
import com.ptitB22DCCN539.todoList.Repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskConvertor {
    private final TaskMapper taskMapper;
    private final UserConvertor userConvertor;
    private final IUserRepository userRepository;

    public TaskEntity taskRequestToTaskEntity(TaskRequest taskRequest) {
        return taskMapper.taskRequestToTaskEntity(taskRequest);
    }

    public TaskResponse taskEntityToTaskResponse(TaskEntity taskEntity) {
        TaskResponse taskResponse = taskMapper.taskEntityToTaskResponse(taskEntity);
        UserEntity user = userRepository.findById(taskEntity.getCreatedBy())
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.EMAIL_NOT_FOUND));
        taskResponse.setUser(userConvertor.entityToResponse(user));
        return taskResponse;
    }
}
