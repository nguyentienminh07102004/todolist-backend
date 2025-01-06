package com.ptitB22DCCN539.todoList.Modal.Request.Task;

import com.ptitB22DCCN539.todoList.Bean.PRIORITY;
import com.ptitB22DCCN539.todoList.Bean.TASK_STATUS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {
    private String id;
    private String taskName;
    private String description;
    private PRIORITY priority;
    private TASK_STATUS status;
    private String category;
    private LocalDateTime dueDate;
    private String notes;
}
