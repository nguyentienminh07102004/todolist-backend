package com.ptitB22DCCN539.todoList.Modal.Response;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TaskResponse implements Serializable {
    private String id;
    private String taskName;
    private String description;
    private String priority;
    private String status;
    private LocalDateTime dueDate;
    private String notes;
    private UserResponse user;
    private CategoryResponse category;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
