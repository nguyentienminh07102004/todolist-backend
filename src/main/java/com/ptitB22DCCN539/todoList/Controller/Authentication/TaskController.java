package com.ptitB22DCCN539.todoList.Controller.Authentication;

import com.ptitB22DCCN539.todoList.Bean.SuccessVariable;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskQueryRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.APIResponse;
import com.ptitB22DCCN539.todoList.Modal.Response.TaskResponse;
import com.ptitB22DCCN539.todoList.Service.Authentication.Task.ITaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/${apiPrefix}/auth/tasks")
public class TaskController {
    private final ITaskService taskService;

    @PostMapping(value = "/")
    public ResponseEntity<APIResponse> saveTask(@Valid @RequestBody TaskRequest taskRequest) {
        TaskResponse taskResponse = taskService.saveTask(taskRequest);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message(SuccessVariable.SAVE_SUCCESS)
                .response(taskResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/")
    public ResponseEntity<APIResponse> findTaskByQuery(@ModelAttribute TaskQueryRequest taskQueryRequest) {
        List<TaskResponse> list = taskService.queryByCondition(taskQueryRequest);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.QUERY_SUCCESS)
                .response(list)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
