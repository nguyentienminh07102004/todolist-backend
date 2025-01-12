package com.ptitB22DCCN539.todoList.Mapper.Category;

import com.ptitB22DCCN539.todoList.Modal.Entity.CategoryEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Category.CategoryRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mappings(value = {
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "tasks", ignore = true)
    })
    CategoryEntity RequestToEntity(CategoryRequest categoryRequest);

    CategoryResponse EntityToResponse(CategoryEntity category);
}
