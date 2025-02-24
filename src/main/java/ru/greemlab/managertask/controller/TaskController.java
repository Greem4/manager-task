package ru.greemlab.managertask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.greemlab.managertask.domain.dto.*;
import ru.greemlab.managertask.domain.model.TaskPriority;
import ru.greemlab.managertask.domain.model.TaskStatus;
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

    @PutMapping("/{taskId}")
    public TaskResponse updateTask(@PathVariable Long taskId,
                                   @RequestBody @Valid TaskUpdateRequest request) {
        return taskService.updateTask(taskId, request);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskService.delete(taskId);
    }

    @GetMapping("/author/{authorId}")
    public Page<TaskResponse> getTasksByAuthor(@PathVariable Long authorId,
                                               Pageable pageable) {
        return taskService.getTasksByAuthor(authorId, pageable);
    }

    @GetMapping("/assignee/{assigneeId}")
    public Page<TaskResponse> getTasksByAssignee(@PathVariable Long assigneeId,
                                                 Pageable pageable) {
        return taskService.getTasksByAssignee(assigneeId, pageable);
    }

    @PatchMapping("/{taskId}/status")
    public TaskResponse updateStatus(@PathVariable Long taskId,
                                     @RequestParam TaskStatus status) {
        return taskService.updateStatus(taskId, status);
    }

    @PatchMapping("/{taskId}/priority")
    public TaskResponse updatePriority(@PathVariable Long taskId,
                                       @RequestParam TaskPriority priority) {
        return taskService.updatePriority(taskId, priority);
    }

    @PatchMapping("/{taskId}/assignee/{userId}")
    public TaskResponse assignTask(@PathVariable Long taskId,
                                   @PathVariable Long userId) {
        return taskService.assignTask(taskId, userId);
    }

    @GetMapping("/{taskId}/comments")
    public Page<CommentResponse> getComments(@PathVariable Long taskId, Pageable pageable) {
        return taskService.getCommentsByTask(taskId, pageable);
    }

    @PostMapping("/{taskId}/comments")
    public CommentResponse addComment(@PathVariable Long taskId,
                                      @RequestBody @Valid CommentRequest request) {
        return taskService.addComment(taskId, request);
    }
}
