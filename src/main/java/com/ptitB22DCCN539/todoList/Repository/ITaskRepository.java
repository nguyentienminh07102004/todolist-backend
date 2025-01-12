package com.ptitB22DCCN539.todoList.Repository;

import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskRepository extends JpaRepository<TaskEntity, String>, JpaSpecificationExecutor<TaskEntity> {
    List<TaskEntity> findByCreatedBy(String createdBy);
    boolean existsByIdAndCreatedBy(String id, String createdBy);
    boolean existsByIdInAndCreatedBy(List<String> ids, String createdBy);
    void deleteAllByIdIn(List<String> ids);
}
