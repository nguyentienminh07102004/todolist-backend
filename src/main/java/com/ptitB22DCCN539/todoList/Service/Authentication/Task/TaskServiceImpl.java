package com.ptitB22DCCN539.todoList.Service.Authentication.Task;

import com.ptitB22DCCN539.todoList.CustomerException.DataInvalidException;
import com.ptitB22DCCN539.todoList.CustomerException.ExceptionVariable;
import com.ptitB22DCCN539.todoList.Mapper.Task.TaskConvertor;
import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskQueryRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.TaskResponse;
import com.ptitB22DCCN539.todoList.Repository.ITaskRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
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
    public PagedModel<TaskResponse> getAllMyTasks(Integer page) {
        if (page == null || page < 1) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, 10);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<TaskEntity> taskEntities = taskRepository.findByCreatedBy(email, pageable);
        Page<TaskResponse> taskResponses = taskEntities.map(taskConvertor::taskEntityToTaskResponse);
        return new PagedModel<>(taskResponses);
    }

    @Override
    @Transactional
    public void deleteTask(List<String> ids) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // kiểm tra xem tất cả task id có thuộc cùng 1 người không
        List<TaskEntity> tasks = taskRepository.findByCreatedBy(email);
        boolean isContain = tasks.stream()
                .map(TaskEntity::getId)
                .collect(Collectors.toSet())
                .containsAll(ids);
        if (!isContain) {
            throw new DataInvalidException(ExceptionVariable.TASK_NOT_FOUND);
        }
        taskRepository.deleteAll(tasks);
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
}
