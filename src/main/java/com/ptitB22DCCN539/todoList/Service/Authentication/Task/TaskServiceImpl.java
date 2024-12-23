package com.ptitB22DCCN539.todoList.Service.Authentication.Task;

import com.ptitB22DCCN539.todoList.Mapper.Task.TaskConvertor;
import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.TaskResponse;
import com.ptitB22DCCN539.todoList.Repository.ITaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements ITaskService {
    private final ITaskRepository taskRepository;
    private final TaskConvertor taskConvertor;

    @Override
    @Transactional
    public TaskResponse saveTask(TaskRequest taskRequest) {
        TaskEntity taskEntity = taskConvertor.taskRequestToTaskEntity(taskRequest);
        taskRepository.save(taskEntity);
        return taskConvertor.taskEntityToTaskResponse(taskEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedModel<TaskResponse> getMyTasks(Integer page) {
        if(page == null || page < 1) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, 10);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<TaskEntity> taskEntities = taskRepository.findByCreatedBy(email, pageable);
        Page<TaskResponse> taskResponses = taskEntities.map(taskConvertor::taskEntityToTaskResponse);
        return new PagedModel<>(taskResponses);
    }
}
