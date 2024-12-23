package com.ptitB22DCCN539.todoList.Service.Authentication.Task;

import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.TaskResponse;
import org.springframework.data.web.PagedModel;

public interface ITaskService {
    TaskResponse saveTask(TaskRequest taskRequest);
    PagedModel<TaskResponse> getMyTasks(Integer page);
}
