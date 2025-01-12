package com.ptitB22DCCN539.todoList.Controller.Authentication;

import com.ptitB22DCCN539.todoList.Bean.SuccessVariable;
import com.ptitB22DCCN539.todoList.Modal.Request.Category.CategoryRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.APIResponse;
import com.ptitB22DCCN539.todoList.Modal.Response.CategoryResponse;
import com.ptitB22DCCN539.todoList.Service.Authentication.Category.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/${apiPrefix}/auth/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    @Autowired
    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/")
    public ResponseEntity<APIResponse> getAllCategories(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PagedModel<CategoryResponse> list = categoryService.getAllCategories(page, pageSize);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.QUERY_SUCCESS)
                .response(list)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/name")
    public ResponseEntity<APIResponse> getCategoryByName(@RequestParam(required = false) String name) {
        List<CategoryResponse> list = categoryService.getCategoryByName(name);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .response(list)
                .message(SuccessVariable.QUERY_SUCCESS)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/")
    public ResponseEntity<APIResponse> saveCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.save(categoryRequest);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message(SuccessVariable.SAVE_SUCCESS)
                .response(categoryResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/")
    public ResponseEntity<APIResponse> updateCategory(CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.save(categoryRequest);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.UPDATE_SUCCESS)
                .response(categoryResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{ids}")
    public ResponseEntity<APIResponse> deleteCategory(@PathVariable List<String> ids) {
        categoryService.deleteCategoryByIds(ids);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.DELETE_SUCCESS)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<APIResponse> getCategoryById(@PathVariable String id) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(id);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message(SuccessVariable.QUERY_SUCCESS)
                .response(categoryResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
