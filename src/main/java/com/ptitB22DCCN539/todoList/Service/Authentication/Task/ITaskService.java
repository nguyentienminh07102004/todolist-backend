package com.ptitB22DCCN539.todoList.Service.Authentication.Task;

import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskQueryRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.TaskResponse;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface ITaskService {
    TaskResponse saveTask(TaskRequest taskRequest);
    PagedModel<TaskResponse> getAllMyTasks(Integer page, Integer pageSize);
    void deleteTask(List<String> ids);
    List<TaskResponse> queryByCondition(TaskQueryRequest taskQueryRequest);
    TaskEntity getTaskById(String id);
    TaskResponse getTaskResponseById(String id);
    List<TaskResponse> getAllTaskDelete();
    void restoreTask(List<String> ids);
}