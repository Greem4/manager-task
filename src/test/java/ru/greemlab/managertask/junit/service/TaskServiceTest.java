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
        when(userService.getByEmail("testuser@test.com")).thenReturn(user);
        when(taskMapper.toEntity(taskCreateRequest, user, user)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        var taskResponse = getTaskResponse();
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        var taskResponseResult = taskService.createTask(taskCreateRequest);

        assertNotNull(taskResponseResult);
        assertEquals("Task Title", taskResponseResult.title());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTask() {
        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(task));
        when(userService.getByEmail("testuser@example.com")).thenReturn(user);
        when(taskMapper.toEntityForUpdate(taskUpdateRequest, task, user)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        var taskResponse = getTaskResponse();
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse updatedTask = taskService.updateTask(1L, taskUpdateRequest);

        assertNotNull(updatedTask);
        assertEquals("Updated Title", updatedTask.title());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        taskService.delete(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetTasksByAuthor() {
        Pageable pageable = mock(Pageable.class);
        Page<Task> taskPage = new PageImpl<>(List.of(task));
        when(taskRepository.findByAuthor(user, pageable)).thenReturn(taskPage);
        var taskResponse = getTaskResponse();
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        Page<TaskResponse> tasks = taskService.getTasksByAuthor(user.getId(), pageable);

        assertNotNull(tasks);
        assertEquals(1, tasks.getTotalElements());
        verify(taskRepository, times(1)).findByAuthor(user, pageable);
    }

    @Test
    void testAddComment() {
        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(task));
        when(userService.getByEmail("testuser@example.com")).thenReturn(user);
        TaskComment comment = new TaskComment();
        comment.setTask(task);
        comment.setUser(user);
        comment.setComment(commentRequest.comment());
        when(commentRepository.save(comment)).thenReturn(comment);
        CommentResponse commentResponse = new CommentResponse(comment.getId(), comment.getComment(), comment.getTask().getId(), comment.getUser().getId(), comment.getUser().getEmail(), comment.getCreatedAt());
        when(commentMapper.toCommentResponse(comment)).thenReturn(commentResponse);

        CommentResponse commentResponseResult = taskService.addComment(1L, commentRequest);

        assertNotNull(commentResponseResult);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testUpdateStatus() {
        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(task));
        task.setStatus(TaskStatus.IN_PROGRESS);
        when(taskRepository.save(task)).thenReturn(task);
        var taskResponse = getTaskResponse();
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse updatedTask = taskService.updateStatus(1L, TaskStatus.IN_PROGRESS);

        assertNotNull(updatedTask);
        assertEquals(TaskStatus.IN_PROGRESS.name(), updatedTask.status());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdatePriority() {
        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(task));
        task.setPriority(TaskPriority.HIGH);
        when(taskRepository.save(task)).thenReturn(task);
        var taskResponse = getTaskResponse();
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse updatedTask = taskService.updatePriority(1L, TaskPriority.HIGH);

        assertNotNull(updatedTask);
        assertEquals(TaskPriority.HIGH.name(), updatedTask.priority());
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
