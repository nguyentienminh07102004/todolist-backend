package com.ptitB22DCCN539.todoList.Controller.Authentication;

import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.APIResponse;
import com.ptitB22DCCN539.todoList.Modal.Response.TaskResponse;
import com.ptitB22DCCN539.todoList.Service.Authentication.Task.ITaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/${apiPrefix}/auth/tasks")
public class TaskController {
    private final ITaskService taskService;

    @PostMapping(value = "/")
    public ResponseEntity<APIResponse> saveTask(@RequestBody TaskRequest taskRequest) {
        TaskResponse taskResponse = taskService.saveTask(taskRequest);
        APIResponse response = APIResponse.builder()
                .message("SAVE TASK SUCCESS")
                .response(taskResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
