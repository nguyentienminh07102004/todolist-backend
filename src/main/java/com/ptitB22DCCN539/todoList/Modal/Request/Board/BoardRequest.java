package com.ptitB22DCCN539.todoList.Modal.Request.Board;

import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRequest {
    private String id;
    private String title;
    private String description;
    private List<TaskRequest> tasks;
}
