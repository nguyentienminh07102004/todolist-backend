package com.ptitB22DCCN539.todoList.Repository;

import com.ptitB22DCCN539.todoList.Modal.Entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryEntity, String>, JpaSpecificationExecutor<CategoryEntity> {
    List<CategoryEntity> findByNameContaining(String name);
    List<CategoryEntity> findByUser_Email(String email);
}
