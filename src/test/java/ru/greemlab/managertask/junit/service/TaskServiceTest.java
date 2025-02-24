package ru.greemlab.managertask.junit.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.greemlab.managertask.domain.dto.CommentRequest;
import ru.greemlab.managertask.domain.dto.CommentResponse;
import ru.greemlab.managertask.domain.dto.TaskCreateRequest;
import ru.greemlab.managertask.domain.dto.TaskResponse;
import ru.greemlab.managertask.domain.dto.TaskUpdateRequest;
import ru.greemlab.managertask.domain.model.Role;
import ru.greemlab.managertask.domain.model.Task;
import ru.greemlab.managertask.domain.model.TaskComment;
import ru.greemlab.managertask.domain.model.TaskPriority;
import ru.greemlab.managertask.domain.model.TaskStatus;
import ru.greemlab.managertask.domain.model.User;
import ru.greemlab.managertask.mapper.CommentMapper;
import ru.greemlab.managertask.mapper.TaskMapper;
import ru.greemlab.managertask.repository.TaskCommentRepository;
import ru.greemlab.managertask.repository.TaskRepository;
import ru.greemlab.managertask.service.TaskService;
import ru.greemlab.managertask.service.UserService;
import ru.greemlab.managertask.util.security.SecurityUtils;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private TaskCommentRepository commentRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private User user;
    private TaskCreateRequest taskCreateRequest;
    private TaskUpdateRequest taskUpdateRequest;
    private CommentRequest commentRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@test.com");
        user.setRole(Role.ROLE_USER);

        task = new Task();
        task.setId(1L);
        task.setAssignee(user);
        task.setAuthor(user);
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(TaskPriority.MEDIUM);

        taskCreateRequest = new TaskCreateRequest("Task Title", "Task Description", TaskStatus.PENDING, TaskPriority.MEDIUM, 1L);
        taskUpdateRequest = new TaskUpdateRequest("Updated Title", "Updated Description", TaskStatus.IN_PROGRESS, TaskPriority.MEDIUM, 1L);
        commentRequest = new CommentRequest("This is a comment");
    }

    @Test
    void testCreateTask() {
        // Настроим поведение для SecurityUtils
        when(SecurityUtils.getCurrentUserName()).thenReturn("test@test.com");

        // Настроим поведение для userService
        when(userService.getByEmail("test@test.com")).thenReturn(user);

        // Если пользователь с ролью ADMIN, то assignee может быть другим пользователем
        when(userService.getById(1L)).thenReturn(user);

        // Настроим маппер
        when(taskMapper.toEntity(taskCreateRequest, user, user)).thenReturn(task);

        // Настроим репозиторий
        when(taskRepository.save(task)).thenReturn(task);

        // Настроим маппер для ответа
        var taskResponse = getTaskResponse();
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        // Вызовем метод для создания задачи
        var taskResponseResult = taskService.createTask(taskCreateRequest);

        // Проверим, что ответ не null и проверим название задачи
        assertNotNull(taskResponseResult);
        assertEquals("Task Title", taskResponseResult.title());

        // Убедимся, что репозиторий сохранил задачу
        verify(taskRepository, times(1)).save(task);
    }


    private @NotNull TaskResponse getTaskResponse() {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getPriority().name(),
                task.getAuthor().getId(),
                task.getAssignee() != null ? task.getAssignee().getId() : null);
    }
}
