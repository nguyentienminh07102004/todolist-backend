package com.ptitB22DCCN539.todoList.Mapper.Category;

import com.ptitB22DCCN539.todoList.Modal.Entity.CategoryEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Category.CategoryRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "tasks", ignore = true)
    CategoryEntity RequestToEntity(CategoryRequest categoryRequest);
    CategoryResponse EntityToResponse(CategoryEntity category);
}
