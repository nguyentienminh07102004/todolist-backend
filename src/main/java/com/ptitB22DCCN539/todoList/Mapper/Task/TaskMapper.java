package com.ptitB22DCCN539.todoList.Mapper.Task;

import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.TaskResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskEntity taskRequestToTaskEntity(TaskRequest taskRequest);
    TaskResponse taskEntityToTaskResponse(TaskEntity taskEntity);
}
