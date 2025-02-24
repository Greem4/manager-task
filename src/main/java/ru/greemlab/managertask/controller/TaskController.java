package ru.greemlab.managertask.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.greemlab.managertask.domain.dto.*;
import ru.greemlab.managertask.domain.model.TaskPriority;
import ru.greemlab.managertask.domain.model.TaskStatus;
import ru.greemlab.managertask.service.TaskService;

/**
 * Контроллер для управления задачами.
 * Позволяет создавать, обновлять, удалять и получать задачи.
 */
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Создание новой задачи.
     *
     * @param request Данные для создания задачи.
     * @return Ответ с информацией о созданной задаче.
     */
    @PostMapping
    @Operation(summary = "Создание новой задачи",
            description = "Этот метод позволяет создать новую задачу с переданными данными.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    public TaskResponse createTask(@RequestBody @Valid TaskCreateRequest request) {
        return taskService.createTask(request);
    }

    /**
     * Обновление существующей задачи.
     *
     * @param taskId ID задачи, которую нужно обновить.
     * @param request Данные для обновления задачи.
     * @return Ответ с информацией об обновленной задаче.
     */
    @PutMapping("/{taskId}")
    @Operation(summary = "Обновление существующей задачи",
            description = "Этот метод позволяет обновить данные существующей задачи.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Задача с указанным ID не найдена")
    })
    public TaskResponse updateTask(@PathVariable Long taskId,
                                   @RequestBody @Valid TaskUpdateRequest request) {
        return taskService.updateTask(taskId, request);
    }

    /**
     * Удаление задачи по ID.
     *
     * @param taskId ID задачи, которую нужно удалить.
     */
    @DeleteMapping("/{taskId}")
    @Operation(summary = "Удаление задачи",
            description = "Этот метод позволяет удалить задачу по указанному ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Задача успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Задача с указанным ID не найдена")
    })
    public void deleteTask(@PathVariable Long taskId) {
        taskService.delete(taskId);
    }

    /**
     * Получение задач по автору.
     *
     * @param authorId ID автора задач.
     * @param pageable Параметры для постраничного вывода.
     * @return Список задач, созданных данным автором.
     */
    @GetMapping("/author/{authorId}")
    @Operation(summary = "Получение задач по автору",
            description = "Этот метод позволяет получить задачи, созданные автором с указанным ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задачи успешно получены"),
            @ApiResponse(responseCode = "404", description = "Автор с указанным ID не найден")
    })
    public Page<TaskResponse> getTasksByAuthor(@PathVariable Long authorId,
                                               Pageable pageable) {
        return taskService.getTasksByAuthor(authorId, pageable);
    }

    /**
     * Получение задач по исполнителю.
     *
     * @param assigneeId ID исполнителя задач.
     * @param pageable Параметры для постраничного вывода.
     * @return Список задач, назначенных этому исполнителю.
     */
    @GetMapping("/assignee/{assigneeId}")
    @Operation(summary = "Получение задач по исполнителю",
            description = "Этот метод позволяет получить задачи, назначенные исполнителю с указанным ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задачи успешно получены"),
            @ApiResponse(responseCode = "404", description = "Исполнитель с указанным ID не найден")
    })
    public Page<TaskResponse> getTasksByAssignee(@PathVariable Long assigneeId,
                                                 Pageable pageable) {
        return taskService.getTasksByAssignee(assigneeId, pageable);
    }

    /**
     * Обновление статуса задачи.
     *
     * @param taskId ID задачи.
     * @param status Новый статус задачи.
     * @return Ответ с информацией о обновленной задаче.
     */
    @PatchMapping("/{taskId}/status")
    @Operation(summary = "Обновление статуса задачи",
            description = "Этот метод позволяет обновить статус задачи с указанным ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус задачи успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Задача с указанным ID не найдена")
    })
    public TaskResponse updateStatus(@PathVariable Long taskId,
                                     @RequestParam TaskStatus status) {
        return taskService.updateStatus(taskId, status);
    }

    /**
     * Обновление приоритета задачи.
     *
     * @param taskId ID задачи.
     * @param priority Новый приоритет задачи.
     * @return Ответ с информацией о обновленной задаче.
     */
    @PatchMapping("/{taskId}/priority")
    @Operation(summary = "Обновление приоритета задачи",
            description = "Этот метод позволяет обновить приоритет задачи с указанным ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Приоритет задачи успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Задача с указанным ID не найдена")
    })
    public TaskResponse updatePriority(@PathVariable Long taskId,
                                       @RequestParam TaskPriority priority) {
        return taskService.updatePriority(taskId, priority);
    }

    /**
     * Назначение задачи на нового исполнителя.
     *
     * @param taskId ID задачи.
     * @param userId ID пользователя, которому назначается задача.
     * @return Ответ с информацией о назначенной задаче.
     */
    @PatchMapping("/{taskId}/assignee/{userId}")
    @Operation(summary = "Назначение задачи на нового исполнителя",
            description = "Этот метод позволяет назначить задачу на нового исполнителя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно назначена"),
            @ApiResponse(responseCode = "404", description = "Задача или пользователь не найдены")
    })
    public TaskResponse assignTask(@PathVariable Long taskId,
                                   @PathVariable Long userId) {
        return taskService.assignTask(taskId, userId);
    }

    /**
     * Получение комментариев к задаче.
     *
     * @param taskId ID задачи.
     * @param pageable Параметры для постраничного вывода.
     * @return Список комментариев к задаче.
     */
    @GetMapping("/{taskId}/comments")
    @Operation(summary = "Получение комментариев к задаче",
            description = "Этот метод позволяет получить комментарии, добавленные к задаче с указанным ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарии успешно получены"),
            @ApiResponse(responseCode = "404", description = "Задача с указанным ID не найдена")
    })
    public Page<CommentResponse> getComments(@PathVariable Long taskId, Pageable pageable) {
        return taskService.getCommentsByTask(taskId, pageable);
    }

    /**
     * Добавление комментария к задаче.
     *
     * @param taskId ID задачи.
     * @param request Данные комментария.
     * @return Ответ с информацией о добавленном комментарии.
     */
    @PostMapping("/{taskId}/comments")
    @Operation(summary = "Добавление комментария к задаче",
            description = "Этот метод позволяет добавить новый комментарий к задаче с указанным ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Комментарий успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные комментария")
    })
    public CommentResponse addComment(@PathVariable Long taskId,
                                      @RequestBody @Valid CommentRequest request) {
        return taskService.addComment(taskId, request);
    }
}
