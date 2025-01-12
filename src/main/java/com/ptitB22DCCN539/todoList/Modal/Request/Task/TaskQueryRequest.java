package com.ptitB22DCCN539.todoList.Modal.Request.Task;

import com.ptitB22DCCN539.todoList.Bean.PRIORITY;
import com.ptitB22DCCN539.todoList.Bean.TASK_STATUS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskQueryRequest {
    private String taskName;
    private PRIORITY priority;
    private TASK_STATUS status;
    private LocalDateTime dueDateFrom;
    private LocalDateTime dueDateTo;
    private List<String> categories;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer pageSize = 10;
}
