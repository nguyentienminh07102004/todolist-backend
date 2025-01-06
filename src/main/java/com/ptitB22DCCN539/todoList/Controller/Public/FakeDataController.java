package com.ptitB22DCCN539.todoList.Controller.Public;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.ptitB22DCCN539.todoList.Bean.PRIORITY;
import com.ptitB22DCCN539.todoList.Bean.TASK_STATUS;
import com.ptitB22DCCN539.todoList.Modal.Request.Category.CategoryRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.Task.TaskRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.CategoryResponse;
import com.ptitB22DCCN539.todoList.Service.Authentication.Category.ICategoryService;
import com.ptitB22DCCN539.todoList.Service.Authentication.Task.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/fakes")
public class FakeDataController {
    private final ITaskService taskService;
    private final ICategoryService categoryService;

    @Autowired
    public FakeDataController(ITaskService taskService, ICategoryService categoryService) {
        this.taskService = taskService;
        this.categoryService = categoryService;
    }

    @PostMapping(value = "/categories/")
    public void save() {
        Faker faker = new Faker(Locale.getDefault());
        for (int i = 0; i < 1000; i++) {
            CategoryRequest categoryRequest = CategoryRequest.builder()
                    .name(faker.company().name())
                    .build();
            categoryService.save(categoryRequest);
        }
    }

    @PostMapping(value = "/tasks/")
    public void saveTask() {
        Faker faker = new Faker(Locale.getDefault());
        FakeValuesService fakeValuesService = new FakeValuesService(Locale.getDefault(), new RandomService());
        List<CategoryResponse> categoryResponses = categoryService.getAllCategories();
        List<String> categoryIds = categoryResponses.stream()
                .map(CategoryResponse::getId)
                .toList();
        for (int i = 0; i < 1000; i++) {
            TaskRequest taskRequest = TaskRequest.builder()
                    .taskName(faker.company().name())
                    .dueDate(faker.date().future(10, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .category(categoryIds.get(faker.number().numberBetween(0, categoryIds.size() - 1)))
                    .priority(PRIORITY.valueOf(fakeValuesService.regexify(("(HIGH|MEDIUM|LOW)"))))
                    .status(TASK_STATUS.valueOf(fakeValuesService.regexify("(NOT_STARTED|IN_PROGRESS|COMPLETED|FAILED)")))
                    .description(faker.company().buzzword())
                    .notes(faker.book().title())
                    .build();
            taskService.saveTask(taskRequest);
        }
    }
}
