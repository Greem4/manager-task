package ru.greemlab.managertask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.greemlab.managertask.domain.dto.*;
import ru.greemlab.managertask.domain.model.*;
import ru.greemlab.managertask.mapper.CommentMapper;
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
    private final CommentMapper commentMapper;
    private final TaskCommentRepository commentRepository;

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

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Нет задачи: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }

    @PreAuthorize("""
            hasAnyRole('ADMIN')
            or (
            #authorId == @userService.getByEmail(authentication.name).id
            )
            """)
    public Page<TaskResponse> getTasksByAuthor(Long authorId, Pageable pageable) {
        var author = userService.getById(authorId);
        var page = taskRepository.findByAuthor(author, pageable);
        var content = page.getContent().stream()
                .map(taskMapper::toResponse)
                .toList();
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    @PreAuthorize("""
            hasAnyRole('ADMIN')
            or (
            #assigneeId == @userService.getByEmail(authentication.name).id
            )
            """)
    public Page<TaskResponse> getTasksByAssignee(Long assigneeId, Pageable pageable) {
        var assignee = userService.getById(assigneeId);
        var page = taskRepository.findByAssignee(assignee, pageable);
        var content = page.getContent().stream()
                .map(taskMapper::toResponse)
                .toList();
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    @PreAuthorize("""
            hasRole('ADMIN')
            or (
            @taskService.getTaskById(#taskId).assignee != null
            and @taskService.getTaskById(#taskId).assignee.email == authentication.name
            )
            """)
    public TaskResponse updateStatus(Long taskId, TaskStatus status) {
        var task = getTaskById(taskId);
        task.setStatus(status);
        var saved = taskRepository.save(task);
        return taskMapper.toResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public TaskResponse updatePriority(Long taskId, TaskPriority priority) {
        var task = getTaskById(taskId);
        task.setPriority(priority);
        var saved = taskRepository.save(task);
        return taskMapper.toResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public TaskResponse assignTask(Long taskId, Long userId) {
        var task = getTaskById(taskId);
        var user = userService.getById(userId);
        task.setAssignee(user);
        var saved = taskRepository.save(task);
        return taskMapper.toResponse(saved);
    }

    @PreAuthorize("""
            hasRole('ADMIN')
            or (
            @taskService.getTaskById(#taskId).author.email == authentication.name
            or (
            @taskService.getTaskById(#taskId).assignee != null
            and @taskService.getTaskById(#taskId).assignee.email == authentication.name
            )
            )
            """)
    public Page<CommentResponse> getCommentsByTask(Long taskId, Pageable pageable) {
        var commentsPage = commentRepository.findByTaskId(taskId, pageable);

        var commentResponses = commentMapper.toCommentResponseList(commentsPage.getContent());

        return new PageImpl<>(commentResponses, pageable, commentsPage.getTotalElements());
    }

    @PreAuthorize("""
            hasRole('ADMIN')
            or (
            @taskService.getTaskById(#taskId).assignee != null
            and @taskService.getTaskById(#taskId).assignee.email == authentication.name
            )
            """)
    public CommentResponse addComment(Long taskId, CommentRequest request) {
        var task = getTaskById(taskId);
        var currentUser = userService.getByEmail(SecurityUtils.getCurrentUserName());

        var comment = TaskComment.builder()
                .task(task)
                .user(currentUser)
                .comment(request.comment())
                .build();
        var savedComment = commentRepository.save(comment);

        return commentMapper.toCommentResponse(savedComment);
    }
}
