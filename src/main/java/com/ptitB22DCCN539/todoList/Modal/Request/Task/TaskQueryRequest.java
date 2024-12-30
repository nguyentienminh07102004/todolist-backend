package com.ptitB22DCCN539.todoList.Modal.Request.Task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskQueryRequest {
    @NotNull
    @NotBlank
    private String taskName;
    private String priority;
    private String status;
    private LocalDateTime dueDate;
    private List<String> categories;
}
