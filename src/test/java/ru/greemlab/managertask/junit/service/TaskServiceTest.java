package ru.greemlab.managertask.junit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
import ru.greemlab.managertask.service.SecurityService;
import ru.greemlab.managertask.service.TaskService;
import ru.greemlab.managertask.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    private SecurityService securityService;

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
        when(securityService.getCurrentUserName()).thenReturn("testuser@test.com");
        when(userService.getByEmail("testuser@test.com")).thenReturn(user);
        when(taskMapper.toEntity(taskCreateRequest, user, user)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);

        var taskResponse = new TaskResponse(
                task.getId(),
                "Task Title",
                "Task Description",
                TaskStatus.PENDING.name(),
                TaskPriority.MEDIUM.name(),
                task.getAuthor().getId(),
                task.getAssignee() != null ? task.getAssignee().getId() : null
        );

        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        var taskResponseResult = taskService.createTask(taskCreateRequest);

        assertNotNull(taskResponseResult);
        assertEquals("Task Title", taskResponseResult.title());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTask() {
        when(securityService.getCurrentUserName()).thenReturn("testuser@test.com");
        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(task));
        when(userService.getByEmail("testuser@test.com")).thenReturn(user);
        when(userService.getById(user.getId())).thenReturn(user);
        when(taskMapper.toEntityForUpdate(taskUpdateRequest, task, user)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);

        var taskResponse = new TaskResponse(
                task.getId(),
                "Updated Title",
                "Updated Description",
                task.getStatus().name(),
                task.getPriority().name(),
                task.getAuthor().getId(),
                task.getAssignee() != null ? task.getAssignee().getId() : null
        );

        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse updatedTask = taskService.updateTask(1L, taskUpdateRequest);

        assertNotNull(updatedTask, "Updated task response should not be null");
        assertEquals("Updated Title", updatedTask.title());
        assertEquals("Updated Description", updatedTask.description());
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

        when(userService.getById(user.getId())).thenReturn(user);
        when(taskRepository.findByAuthor(user, pageable)).thenReturn(taskPage);

        var taskResponse = new TaskResponse(
                task.getId(),
                "Task Title",
                "Task Description",
                task.getStatus().name(),
                task.getPriority().name(),
                task.getAuthor().getId(),
                task.getAssignee() != null ? task.getAssignee().getId() : null
        );
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        Page<TaskResponse> tasks = taskService.getTasksByAuthor(user.getId(), pageable);

        assertNotNull(tasks, "Tasks page should not be null");
        assertEquals(1, tasks.getTotalElements(), "Page should have 1 task");
        verify(taskRepository, times(1)).findByAuthor(user, pageable);
        verify(userService, times(1)).getById(user.getId());
    }

    @Test
    void testAddComment() {
        when(securityService.getCurrentUserName()).thenReturn("testuser@test.com");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userService.getByEmail("testuser@test.com")).thenReturn(user);

        TaskComment comment = new TaskComment();
        comment.setId(1L); // Устанавливаем ID
        comment.setTask(task);
        comment.setUser(user);
        comment.setComment(commentRequest.comment());
        comment.setCreatedAt(LocalDateTime.now());

        when(commentRepository.save(any(TaskComment.class))).thenAnswer(invocation -> {
            TaskComment savedComment = invocation.getArgument(0);
            savedComment.setId(1L);
            savedComment.setCreatedAt(LocalDateTime.now());
            return savedComment;
        });

        CommentResponse commentResponse = new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                comment.getUser().getEmail(),
                comment.getCreatedAt()
        );

        when(commentMapper.toCommentResponse(any(TaskComment.class))).thenReturn(commentResponse);

        CommentResponse commentResponseResult = taskService.addComment(1L, commentRequest);

        assertNotNull(commentResponseResult, "Comment response should not be null");
        verify(commentRepository, times(1)).save(any(TaskComment.class));
        verify(commentMapper, times(1)).toCommentResponse(any(TaskComment.class));
    }

    @Test
    void testUpdateStatus() {
        when(securityService.getCurrentUserName()).thenReturn("testuser@test.com");
        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(task));
        task.setStatus(TaskStatus.IN_PROGRESS);
        when(taskRepository.save(task)).thenReturn(task);

        var taskResponse = new TaskResponse(
                task.getId(),
                "Task Title",
                "Task Description",
                TaskStatus.IN_PROGRESS.name(),
                TaskPriority.MEDIUM.name(),
                task.getAuthor().getId(),
                task.getAssignee() != null ? task.getAssignee().getId() : null
        );
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse updatedTask = taskService.updateStatus(1L, TaskStatus.IN_PROGRESS);

        assertNotNull(updatedTask);
        assertEquals(TaskStatus.IN_PROGRESS.name(), updatedTask.status());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdatePriority() {
        when(securityService.getCurrentUserName()).thenReturn("testuser@test.com");
        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(task));
        task.setPriority(TaskPriority.HIGH);
        when(taskRepository.save(task)).thenReturn(task);

        var taskResponse = new TaskResponse(
                task.getId(),
                "Task Title",
                "Task Description",
                TaskStatus.PENDING.name(),
                TaskPriority.HIGH.name(),
                task.getAuthor().getId(),
                task.getAssignee() != null ? task.getAssignee().getId() : null
        );
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse updatedTask = taskService.updatePriority(1L, TaskPriority.HIGH);

        assertNotNull(updatedTask);
        assertEquals(TaskPriority.HIGH.name(), updatedTask.priority());
        verify(taskRepository, times(1)).save(task);
    }
}

