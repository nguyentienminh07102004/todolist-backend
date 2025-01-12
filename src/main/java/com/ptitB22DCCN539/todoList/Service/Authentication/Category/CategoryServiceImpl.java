package com.ptitB22DCCN539.todoList.Service.Authentication.Category;

import com.ptitB22DCCN539.todoList.CustomerException.DataInvalidException;
import com.ptitB22DCCN539.todoList.CustomerException.ExceptionVariable;
import com.ptitB22DCCN539.todoList.Mapper.Category.CategoryConvertor;
import com.ptitB22DCCN539.todoList.Modal.Entity.CategoryEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.CategoryEntity_;
import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.UserEntity_;
import com.ptitB22DCCN539.todoList.Modal.Request.Category.CategoryRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.CategoryResponse;
import com.ptitB22DCCN539.todoList.Repository.ICategoryRepository;
import com.ptitB22DCCN539.todoList.Repository.IUserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryConvertor categoryConvertor;
    private final ICategoryRepository categoryRepository;
    private final IUserRepository userRepository;

    @Autowired
    public CategoryServiceImpl(CategoryConvertor categoryConvertor,
                               ICategoryRepository categoryRepository,
                               IUserRepository userRepository) {
        this.categoryConvertor = categoryConvertor;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CategoryResponse save(CategoryRequest category) {
        CategoryEntity categoryEntity = categoryConvertor.RequestToEntity(category);
        categoryEntity.setUser(userRepository.findById(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.UNAUTHENTICATED)));
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
    public List<CategoryResponse> getCategoryByName(String name) {
        Specification<CategoryEntity> specification = (root, query, builder) -> {
            Predicate predicate = builder.equal(root.get(CategoryEntity_.USER).get(UserEntity_.EMAIL), SecurityContextHolder.getContext().getAuthentication().getName());
            if (StringUtils.hasText(name)) {
                predicate = builder.and(predicate, builder.like(root.get(CategoryEntity_.NAME), "%" + name + "%"));
            }
            return predicate;
        };
        List<CategoryEntity> entityPage = categoryRepository.findAll(specification);
        return entityPage.stream().map(categoryConvertor::EntityToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findByUser_Email(SecurityContextHolder.getContext().getAuthentication().getName()).stream()
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
