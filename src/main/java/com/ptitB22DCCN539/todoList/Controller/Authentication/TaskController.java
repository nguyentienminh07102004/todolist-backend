package com.ptitB22DCCN539.todoList.Controller.Authentication;

import com.ptitB22DCCN539.todoList.Bean.SuccessVariable;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskQueryRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.APIResponse;
import com.ptitB22DCCN539.todoList.Modal.Response.TaskResponse;
import com.ptitB22DCCN539.todoList.Service.Authentication.Task.ITaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PutMapping(value = "/")
    public ResponseEntity<APIResponse> updateTask(@Valid @RequestBody TaskRequest taskRequest) {
        TaskResponse taskResponse = taskService.saveTask(taskRequest);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.UPDATE_SUCCESS)
                .response(taskResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/my-task/")
    public ResponseEntity<APIResponse> findTaskByQuery(@ModelAttribute TaskQueryRequest taskQueryRequest) {
        PagedModel<TaskResponse> list = taskService.getAllMyTasks(taskQueryRequest);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.QUERY_SUCCESS)
                .response(list)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize(value = "@securityUtils.checkTaskIdInUser(#id)")
    public ResponseEntity<APIResponse> findTaskById(@PathVariable String id) {
        TaskResponse taskResponse = taskService.getTaskResponseById(id);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.QUERY_SUCCESS)
                .response(taskResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/my-tasks/{ids}")
    @PreAuthorize(value = "@securityUtils.checkTaskIdInUser(#ids)")
    public ResponseEntity<APIResponse> deleteTaskById(@PathVariable List<String> ids) {
        taskService.deleteTask(ids);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.DELETE_SUCCESS)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/deleted/all")
    public ResponseEntity<APIResponse> getTaskDeleted(@RequestParam(defaultValue = "1", required = false) Integer page,
                                                      @RequestParam(defaultValue = "10", required = false) Integer size) {
        PagedModel<TaskResponse> taskResponses = taskService.getAllTaskDelete(page, size);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.QUERY_SUCCESS)
                .response(taskResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/restore/{ids}")
    public ResponseEntity<APIResponse> restoreTaskDeleted(@PathVariable List<String> ids) {
        taskService.restoreTask(ids);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.UPDATE_SUCCESS)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/deleted/completed/{ids}")
    public ResponseEntity<APIResponse> deleteAllTasksCompleted(@PathVariable List<String> ids) {
        taskService.deleteTaskByIdCompleted(ids);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.DELETE_SUCCESS)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
