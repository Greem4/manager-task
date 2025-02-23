package ru.greemlab.managertask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.greemlab.managertask.domain.dto.TaskCreateRequest;
import ru.greemlab.managertask.domain.dto.TaskResponse;
import ru.greemlab.managertask.domain.dto.TaskUpdateRequest;
import ru.greemlab.managertask.service.TaskService;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskResponse createTask(@RequestBody @Valid TaskCreateRequest request) {
        return taskService.createTask(request);
    }

    @PostMapping("/{taskId}")
    public TaskResponse updateTask(@PathVariable Long taskId,
                                   @RequestBody @Valid TaskUpdateRequest request) {
        return taskService.updateTask(taskId, request);
    }
}
