package com.ptitB22DCCN539.todoList.Modal.Request.Task;

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
    private String priority;
    private String status;
    private LocalDateTime dueDate;
    private String notes;
}
