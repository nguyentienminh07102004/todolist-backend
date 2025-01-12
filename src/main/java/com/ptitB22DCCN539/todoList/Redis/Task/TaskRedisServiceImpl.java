package com.ptitB22DCCN539.todoList.Redis.Task;

import com.ptitB22DCCN539.todoList.Bean.ContantVariable;
import com.ptitB22DCCN539.todoList.Mapper.Task.TaskConvertor;
import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import com.ptitB22DCCN539.todoList.Redis.BaseRedisServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TaskRedisServiceImpl extends BaseRedisServiceImpl<String, String, TaskRedisEntity> implements ITaskRedisService {
    private final TaskConvertor taskConvertor;

    @Autowired
    public TaskRedisServiceImpl(RedisTemplate<String, TaskRedisEntity> redisTemplate,
                                TaskConvertor taskConvertor) {
        super(redisTemplate);
        this.taskConvertor = taskConvertor;
    }

    private String getTaskRedisId(String id) {
        return "%s:%s".formatted(ContantVariable.getTaskRedisPrefix(), id);
    }

    @Override
    public void clear() {
        this.delete("%s:*".formatted(ContantVariable.getTaskRedisPrefix()));
    }


    @Override
    public TaskEntity getTaskById(String id) {
        TaskRedisEntity taskRedisEntity = this.get(this.getTaskRedisId(id));
        return taskConvertor.taskRedisEntityToTaskEntity(taskRedisEntity);
    }

    @Override
    public void saveTask(TaskEntity task) {
        TaskRedisEntity taskRedisEntity = taskConvertor.taskEntityToTaskRedisEntity(task);
        this.set(this.getTaskRedisId(task.getId()), taskRedisEntity);
    }

    @Override
    public void saveTask(List<TaskEntity> tasks, Long expire) {
        for (TaskEntity task : tasks) {
            this.saveTask(task);
            if (expire != null && expire > 0) {
                this.setTimeToLive(this.getTaskRedisId(task.getId()), expire);
            }
        }
    }

    @Override
    public List<TaskEntity> getAllDeleteTasks() {
        List<TaskEntity> tasks = new ArrayList<>();
        for (String id : this.getKeysByPrefix("%s:*".formatted(ContantVariable.getTaskRedisPrefix()))) {
            TaskEntity taskEntity = taskConvertor.taskRedisEntityToTaskEntity(this.get(id));
            tasks.add(taskEntity);
        }
        return tasks;
    }

    @Override
    public void deleteTask(List<String> ids) {
        this.deleteAll(ids.stream().map(this::getTaskRedisId).toList());
    }

    @Override
    public Long getTimeToLiveById(String id) {
        return this.getTimeToLive(this.getTaskRedisId(id));
    }
}
