package com.ptitB22DCCN539.todoList.Service.Authentication.Task;

import com.ptitB22DCCN539.todoList.Bean.ContantVariable;
import com.ptitB22DCCN539.todoList.CustomerException.DataInvalidException;
import com.ptitB22DCCN539.todoList.CustomerException.ExceptionVariable;
import com.ptitB22DCCN539.todoList.Mapper.Task.TaskConvertor;
import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskQueryRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.TaskResponse;
import com.ptitB22DCCN539.todoList.Redis.Task.ITaskRedisService;
import com.ptitB22DCCN539.todoList.Repository.ITaskRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskServiceImpl implements ITaskService {
    private final ITaskRepository taskRepository;
    private final TaskConvertor taskConvertor;
    private final ITaskRedisService taskRedisService;

    @Autowired
    public TaskServiceImpl(ITaskRepository taskRepository,
                           TaskConvertor taskConvertor,
                           ITaskRedisService taskRedisService) {
        this.taskRepository = taskRepository;
        this.taskConvertor = taskConvertor;
        this.taskRedisService = taskRedisService;
    }

    @Override
    @Transactional
    public TaskResponse saveTask(TaskRequest taskRequest) {
        TaskEntity taskEntity = taskConvertor.taskRequestToTaskEntity(taskRequest);
        taskRepository.save(taskEntity);
        return taskConvertor.taskEntityToTaskResponse(taskEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedModel<TaskResponse> getAllMyTasks(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<TaskEntity> pageEntity = taskRepository.findByCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName(), pageable);
        return new PagedModel<>(pageEntity.map(taskConvertor::taskEntityToTaskResponse));
    }

    @Override
    @Transactional
    public void deleteTask(List<String> ids) {
        // add to redis and set time to live is 30 days, after that delete in db
        List<TaskEntity> listTaskEntities = taskRepository.findAllById(ids);
        taskRedisService.saveTask(listTaskEntities, ContantVariable.EXPIRE_TASK_AFTER_DELETE);
        taskRepository.deleteAll(listTaskEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> queryByCondition(TaskQueryRequest taskQueryRequest) {
        Specification<TaskEntity> specification = (root, query, builder) -> {
            try {
                Field[] fields = TaskQueryRequest.class.getDeclaredFields();
                Predicate predicate = builder.conjunction();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.get(taskQueryRequest) != null) {
                        if (field.getType().equals(String.class)) {
                            Predicate pre = builder.like(root.get(field.getName()), String.join("", "%", (String) field.get(taskQueryRequest), "%"));
                            predicate = builder.and(pre, predicate);
                        } else if (field.getType().equals(LocalDateTime.class)) {
                            predicate = builder.and(predicate, builder.and(builder.lessThanOrEqualTo(root.get(field.getName()), (LocalDateTime) field.get(taskQueryRequest))));
                        } else if (field.getType().equals(List.class)) {
                            Predicate pre = builder.in(root.get(field.getName())).value(field.get(taskQueryRequest));
                            predicate = builder.and(predicate, pre);
                        }
                    }
                }
                return predicate;
            } catch (Exception exception) {
                return builder.disjunction();
            }
        };
        List<TaskEntity> list = taskRepository.findAll(specification);
        return list.stream()
                .map(taskConvertor::taskEntityToTaskResponse)
                .toList();
    }

    @Override
    public TaskEntity getTaskById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.TASK_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskResponseById(String id) {
        return taskConvertor.taskEntityToTaskResponse(this.getTaskById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTaskDelete() {
        List<TaskEntity> taskEntitiesDeleteList = taskRedisService.getAllDeleteTasks();
        return taskEntitiesDeleteList.stream()
                .map(taskConvertor::taskEntityToTaskResponse)
                .toList();
    }

    @Override
    @Transactional
    public void restoreTask(List<String> ids) {
        List<TaskEntity> listTaskEntities = taskRedisService.getAllDeleteTasks();
        if (!listTaskEntities.stream().map(TaskEntity::getId).collect(Collectors.toSet()).containsAll(ids)) {
            throw new DataInvalidException(ExceptionVariable.TASK_NOT_FOUND);
        }
        for (TaskEntity taskEntity : listTaskEntities) {
            if (ids.contains(taskEntity.getId())) {
                if (taskEntity.getCategory() != null) {
                    taskEntity.getCategory().getTasks().add(taskEntity);
                }
                if (taskEntity.getBoard() != null) {
                    taskEntity.getBoard().getTasks().add(taskEntity);
                }
                taskRepository.save(taskEntity);
            }
        }
        taskRedisService.deleteTask(ids);
    }
}
