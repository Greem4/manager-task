package ru.greemlab.managertask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.greemlab.managertask.domain.dto.TaskCreateRequest;
import ru.greemlab.managertask.domain.dto.TaskResponse;
import ru.greemlab.managertask.domain.dto.TaskUpdateRequest;
import ru.greemlab.managertask.domain.model.*;
import ru.greemlab.managertask.mapper.TaskMapper;
import ru.greemlab.managertask.repository.TaskCommentRepository;
import ru.greemlab.managertask.repository.TaskRepository;
import ru.greemlab.managertask.util.security.SecurityUtils;


@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена: " + taskId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public TaskResponse createTask(TaskCreateRequest request) {
        var currentUser = userService.getByEmail(SecurityUtils.getCurrentUserName());
        User assignee;
        if (currentUser.getRole() == Role.ROLE_ADMIN && request.assigneeId() != null) {
            assignee = userService.getById(request.assigneeId());
        } else {
            assignee = currentUser;
        }
        var task = taskMapper.toEntity(request, currentUser, assignee);
        var saved = taskRepository.save(task);
        return taskMapper.toResponse(saved);
    }

    @PreAuthorize("""
            hasAnyRole('ADMIN', 'USER')
            or (
            #request.assigneeId() != null
            and @taskService.getTaskById(#taskId).assignee != null
            and @taskService.getTaskById(#taskId).assignee.email == authentication.name
            )
            """)
    public TaskResponse updateTask(Long taskId, TaskUpdateRequest request) {
        var existinTask = getTaskById(taskId);
        var currentUser = userService.getByEmail(SecurityUtils.getCurrentUserName());
        User newAssignee = null;

        if (currentUser.getRole() == Role.ROLE_ADMIN && request.assigneeId() != null) {
            newAssignee = userService.getById(request.assigneeId());
        } else if (currentUser.getRole() != Role.ROLE_ADMIN) {
            if (existinTask.getAssignee().equals(currentUser)) {
                newAssignee = currentUser;
            } else {
                throw new RuntimeException("Не достаточно прав для изменения задачи");
            }
        }

        var update = taskMapper.toEntityForUpdate(request, existinTask, newAssignee);
        var saved = taskRepository.save(update);
        return taskMapper.toResponse(saved);
    }
}
