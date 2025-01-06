package com.ptitB22DCCN539.todoList.Service.Authentication.Category;

import com.ptitB22DCCN539.todoList.CustomerException.DataInvalidException;
import com.ptitB22DCCN539.todoList.CustomerException.ExceptionVariable;
import com.ptitB22DCCN539.todoList.Mapper.Category.CategoryConvertor;
import com.ptitB22DCCN539.todoList.Modal.Entity.CategoryEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Category.CategoryRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.CategoryResponse;
import com.ptitB22DCCN539.todoList.Repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryConvertor categoryConvertor;
    private final ICategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryConvertor categoryConvertor,
                               ICategoryRepository categoryRepository) {
        this.categoryConvertor = categoryConvertor;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CategoryResponse save(CategoryRequest category) {
        CategoryEntity categoryEntity = categoryConvertor.RequestToEntity(category);
        categoryRepository.save(categoryEntity);
        return categoryConvertor.EntityToResponse(categoryEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(String id) {
        return this.categoryConvertor.EntityToResponse(this.getCategoryEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryEntity getCategoryEntityById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.CATEGORY_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedModel<CategoryResponse> getCategoryByName(String name, Integer page, Integer pageSize) {
        Page<CategoryEntity> entityPage = categoryRepository.findByNameContaining(name, PageRequest.of(page, pageSize));
        return new PagedModel<>(entityPage.map(categoryConvertor::EntityToResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryConvertor::EntityToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedModel<CategoryResponse> getAllCategories(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<CategoryEntity> entityPage = categoryRepository.findAll(pageable);
        return new PagedModel<>(entityPage.map(categoryConvertor::EntityToResponse));
    }

    @Override
    @Transactional
    public void deleteCategoryByIds(List<String> ids) {
        for (String id : ids) {
            // change category id of task relative to null
            CategoryEntity categoryEntity = this.getCategoryEntityById(id);
            List<TaskEntity> listTasks = categoryEntity.getTasks();
            for (TaskEntity taskEntity : listTasks) {
                taskEntity.setCategory(null);
            }
            categoryEntity.getTasks().clear();
            categoryRepository.deleteById(id);
        }
    }
}
