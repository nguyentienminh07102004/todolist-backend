package com.ptitB22DCCN539.todoList.Modal.Response;

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
public class TaskResponse {
    private String id;
    private String taskName;
    private String description;
    private String priority;
    private String status;
    private LocalDateTime dueDate;
    private String notes;
    private UserResponse user;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
