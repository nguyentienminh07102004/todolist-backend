package com.ptitB22DCCN539.todoList.Mapper.Task;

import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "board", ignore = true)
    TaskEntity taskRequestToTaskEntity(TaskRequest taskRequest);
    @Mapping(target = "user", ignore = true)
    TaskResponse taskEntityToTaskResponse(TaskEntity taskEntity);
}
