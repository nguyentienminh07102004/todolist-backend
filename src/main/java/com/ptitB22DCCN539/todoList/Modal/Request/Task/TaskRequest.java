package com.ptitB22DCCN539.todoList.Modal.Request.Task;

import com.ptitB22DCCN539.todoList.Bean.PRIORITY;
import com.ptitB22DCCN539.todoList.Bean.TASK_STATUS;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @NotBlank
    private String taskName;
    private String description;
    private PRIORITY priority;
    private TASK_STATUS status;
    private String category;
    private LocalDateTime dueDate;
    private String notes;
    private String createdBy;
    private LocalDateTime createdDate;
}
