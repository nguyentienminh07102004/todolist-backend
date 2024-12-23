package com.ptitB22DCCN539.todoList.Repository;

import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskRepository extends JpaRepository<TaskEntity, String> {
    Page<TaskEntity> findByCreatedBy(String createdBy, Pageable pageable);
}
