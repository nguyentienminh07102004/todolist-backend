package com.ptitB22DCCN539.todoList.Mapper.Category;

import com.ptitB22DCCN539.todoList.Modal.Entity.CategoryEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Category.CategoryRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryConvertor {
    private final CategoryMapper categoryMapper;
    @Autowired
    public CategoryConvertor(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }
    public CategoryEntity RequestToEntity(CategoryRequest categoryRequest) {
        return categoryMapper.RequestToEntity(categoryRequest);
    }
    public CategoryResponse EntityToResponse(CategoryEntity categoryEntity) {
        return categoryMapper.EntityToResponse(categoryEntity);
    }
}
