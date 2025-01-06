package com.ptitB22DCCN539.todoList.Service.Authentication.Category;

import com.ptitB22DCCN539.todoList.Modal.Entity.CategoryEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Category.CategoryRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.CategoryResponse;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface ICategoryService {
    CategoryResponse save(CategoryRequest category);
    CategoryResponse getCategoryById(String id);
    CategoryEntity getCategoryEntityById(String id);
    PagedModel<CategoryResponse> getCategoryByName(String name, Integer page, Integer pageSize);
    List<CategoryResponse> getAllCategories();
    PagedModel<CategoryResponse> getAllCategories(Integer page, Integer pageSize);
    void deleteCategoryByIds(List<String> ids);
}
