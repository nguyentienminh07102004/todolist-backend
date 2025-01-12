package com.ptitB22DCCN539.todoList.Mapper.Task;

import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.TaskResponse;
import com.ptitB22DCCN539.todoList.Redis.Task.TaskRedisEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mappings(value = {
            @Mapping(target = "board", ignore = true),
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "modifiedBy", ignore = true),
            @Mapping(target = "modifiedDate", ignore = true)
    })
    TaskEntity taskRequestToTaskEntity(TaskRequest taskRequest);

    @Mapping(target = "timeToLive", ignore = true)
    @Mapping(target = "user", ignore = true)
    TaskResponse taskEntityToTaskResponse(TaskEntity taskEntity);
    @Mappings(value = {
            @Mapping(target = "board", ignore = true),
            @Mapping(target = "category", ignore = true),
    })
    TaskRedisEntity taskEntityToTaskRedisEntity(TaskEntity taskEntity);
    @Mappings(value = {
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "board", ignore = true),
    })
    TaskEntity taskRedisEntityToTaskEntity(TaskRedisEntity taskRedisEntity);
}
