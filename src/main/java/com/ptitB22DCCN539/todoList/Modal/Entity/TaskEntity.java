package com.ptitB22DCCN539.todoList.Modal.Entity;

import com.ptitB22DCCN539.todoList.Bean.PRIORITY;
import com.ptitB22DCCN539.todoList.Bean.TASK_STATUS;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class TaskEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @Column(name = "task_name")
    private String taskName;
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "priority")
    private PRIORITY priority;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private TASK_STATUS status;
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    @Column(name = "notes", columnDefinition = "LONGTEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
}
