package com.ptitB22DCCN539.todoList.Mapper.Task;

import com.ptitB22DCCN539.todoList.CustomerException.DataInvalidException;
import com.ptitB22DCCN539.todoList.CustomerException.ExceptionVariable;
import com.ptitB22DCCN539.todoList.Mapper.User.UserConvertor;
import com.ptitB22DCCN539.todoList.Modal.Entity.CategoryEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.UserEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.TaskResponse;
import com.ptitB22DCCN539.todoList.Redis.Task.TaskRedisEntity;
import com.ptitB22DCCN539.todoList.Repository.IBoardRepository;
import com.ptitB22DCCN539.todoList.Repository.ICategoryRepository;
import com.ptitB22DCCN539.todoList.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskConvertor {
    private final TaskMapper taskMapper;
    private final UserConvertor userConvertor;
    private final IUserRepository userRepository;
    private final ICategoryRepository categoryRepository;
    private final IBoardRepository boardRepository;

    @Autowired
    public TaskConvertor(TaskMapper taskMapper,
                         UserConvertor userConvertor,
                         IUserRepository userRepository,
                         ICategoryRepository categoryRepository,
                         IBoardRepository boardRepository) {
        this.taskMapper = taskMapper;
        this.userConvertor = userConvertor;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.boardRepository = boardRepository;
    }

    public TaskEntity taskRequestToTaskEntity(TaskRequest taskRequest) {
        TaskEntity taskEntity = taskMapper.taskRequestToTaskEntity(taskRequest);
        if (taskRequest.getCategory() != null && !taskRequest.getCategory().isEmpty()) {
            CategoryEntity categoryEntity = categoryRepository.findById(taskRequest.getCategory())
                    .orElseThrow(() -> new DataInvalidException(ExceptionVariable.CATEGORY_NOT_FOUND));
            taskEntity.setCategory(categoryEntity);
            categoryEntity.getTasks().add(taskEntity);
        }
        return taskEntity;
    }

    public TaskResponse taskEntityToTaskResponse(TaskEntity taskEntity) {
        TaskResponse taskResponse = taskMapper.taskEntityToTaskResponse(taskEntity);
        UserEntity user = userRepository.findById(taskEntity.getCreatedBy())
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.EMAIL_NOT_FOUND));
        taskResponse.setUser(userConvertor.entityToResponse(user));
        return taskResponse;
    }

    public TaskRedisEntity taskEntityToTaskRedisEntity(TaskEntity taskEntity) {
        TaskRedisEntity taskRedisEntity = taskMapper.taskEntityToTaskRedisEntity(taskEntity);
        if (taskEntity.getCategory() != null)
            taskRedisEntity.setCategory(taskEntity.getCategory().getId());
        if (taskEntity.getBoard() != null)
            taskRedisEntity.setBoard(taskEntity.getBoard().getId());
        return taskRedisEntity;
    }

    public TaskEntity taskRedisEntityToTaskEntity(TaskRedisEntity taskRedisEntity) {
        TaskEntity taskEntity = taskMapper.taskRedisEntityToTaskEntity(taskRedisEntity);
        if (taskRedisEntity.getCategory() != null)
            taskEntity.setCategory(categoryRepository.findById(taskRedisEntity.getCategory()).orElse(null));
        if (taskRedisEntity.getBoard() != null)
            taskEntity.setBoard(boardRepository.findById(taskRedisEntity.getBoard()).orElse(null));
        return taskEntity;
    }
}
