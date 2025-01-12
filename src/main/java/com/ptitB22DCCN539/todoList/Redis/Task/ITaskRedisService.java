package com.ptitB22DCCN539.todoList.Redis.Task;

import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;

import java.util.List;

public interface ITaskRedisService {
    void clear();
    TaskEntity getTaskById(String id);
    void saveTask(TaskEntity task);
    void saveTask(List<TaskEntity> tasks, Long expire);
    List<TaskEntity> getAllDeleteTasks();
    void deleteTask(List<String> ids);
    Long getTimeToLiveById(String id);
}