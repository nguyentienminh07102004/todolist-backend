package com.ptitB22DCCN539.todoList.Repository;

import com.ptitB22DCCN539.todoList.Modal.Entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryEntity, String> {
}
