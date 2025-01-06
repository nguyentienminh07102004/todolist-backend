package com.ptitB22DCCN539.todoList.Redis.Task;

import com.ptitB22DCCN539.todoList.Bean.PRIORITY;
import com.ptitB22DCCN539.todoList.Bean.TASK_STATUS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRedisEntity implements Serializable {
    private String id;
    private String taskName;
    private String description;
    private PRIORITY priority;
    private TASK_STATUS status;
    private LocalDateTime dueDate;
    private String notes;
    private String board;
    private String category;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String createdBy;
    private String modifiedBy;
}
