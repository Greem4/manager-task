package ru.greemlab.managertask.integration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import ru.greemlab.managertask.domain.dto.CommentRequest;
import ru.greemlab.managertask.domain.dto.CommentResponse;
import ru.greemlab.managertask.domain.dto.TaskCreateRequest;
import ru.greemlab.managertask.domain.dto.TaskResponse;
import ru.greemlab.managertask.domain.dto.TaskUpdateRequest;
import ru.greemlab.managertask.domain.model.TaskPriority;
import ru.greemlab.managertask.domain.model.TaskStatus;
import ru.greemlab.managertask.integration.config.IntegrationTestBase;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TaskControllerTest extends IntegrationTestBase {

    @Test
    void testCreateTask() {
        var taskRequest = new TaskCreateRequest(
                "Интеграционный тест",
                "Проверка создания задачи",
                TaskStatus.PENDING,
                TaskPriority.LOW,
                1L);

        var response = testRestTemplate.postForEntity(
                "/api/v1/tasks",
                new HttpEntity<>(taskRequest, getHeadersAdmin()),
                TaskResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var taskResponse = response.getBody();
        assertThat(taskResponse).isNotNull();
        assertThat(Objects.requireNonNull(taskResponse).title()).isEqualTo("Интеграционный тест");
    }

    @Test
    void testUpdateTask() {
        Long taskId = 2L;

        var taskUpdateRequest = new TaskUpdateRequest(
                "Обновленная задача",
                "Проверка обновления задачи",
                TaskStatus.IN_PROGRESS,
                TaskPriority.HIGH,
                2L);

        var response = testRestTemplate.exchange(
                "/api/v1/tasks/{taskId}",
                HttpMethod.PUT,
                new HttpEntity<>(taskUpdateRequest, getHeadersAdmin()),
                TaskResponse.class,
                taskId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var taskResponse = response.getBody();
        assertThat(taskResponse).isNotNull();
        assertThat(Objects.requireNonNull(taskResponse).title()).isEqualTo("Обновленная задача");
        assertThat(taskResponse.status()).isEqualTo(TaskStatus.IN_PROGRESS.toString());
    }

    @Test
    void testUpdateTaskAsUser() {
        Long taskId = 3L;

        var taskUpdateRequest = new TaskUpdateRequest(
                "Обновленная задача",
                "Проверка обновления задачи пользователем",
                TaskStatus.IN_PROGRESS,
                TaskPriority.HIGH,
                2L);

        var response = testRestTemplate.exchange(
                "/api/v1/tasks/{taskId}",
                HttpMethod.PUT,
                new HttpEntity<>(taskUpdateRequest, getHeadersUser()),
                TaskResponse.class,
                taskId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var taskResponse = response.getBody();
        assertThat(taskResponse).isNotNull();
        assertThat(Objects.requireNonNull(taskResponse).title()).isEqualTo("Обновленная задача");
        assertThat(taskResponse.status()).isEqualTo(TaskStatus.IN_PROGRESS.toString());
    }

    @Test
    void testDeleteTask() {
        Long taskId = 2L;

        var response = testRestTemplate.exchange(
                "/api/v1/tasks/{taskId}",
                HttpMethod.DELETE,
                new HttpEntity<>(getHeadersAdmin()),
                Void.class,
                taskId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void testGetTasksByAuthor() {
        Long authorId = 1L;

        var response = testRestTemplate.exchange(
                "/api/v1/tasks/author/{authorId}",
                HttpMethod.GET,
                new HttpEntity<>(getHeadersAdmin()),
                TaskResponse.class,
                authorId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testGetTasksByAssignee() {
        Long assigneeId = 1L;

        var response = testRestTemplate.exchange(
                "/api/v1/tasks/assignee/{assigneeId}",
                HttpMethod.GET,
                new HttpEntity<>(getHeadersAdmin()),
                TaskResponse.class,
                assigneeId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testUpdateTaskStatus() {
        Long taskId = 2L;

        var response = testRestTemplate.exchange(
                "/api/v1/tasks/{taskId}/status?status={status}",
                HttpMethod.PATCH,
                new HttpEntity<>(getHeadersAdmin()),
                TaskResponse.class,
                taskId, TaskStatus.COMPLETED);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var taskResponse = response.getBody();
        assertThat(taskResponse).isNotNull();
        assertThat(Objects.requireNonNull(taskResponse).status()).isEqualTo(TaskStatus.COMPLETED.toString());
    }

    @Test
    void testUpdateTaskPriority() {
        Long taskId = 2L;

        var response = testRestTemplate.exchange(
                "/api/v1/tasks/{taskId}/priority?priority={priority}",
                HttpMethod.PATCH,
                new HttpEntity<>(getHeadersAdmin()),
                TaskResponse.class,
                taskId, TaskPriority.HIGH);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var taskResponse = response.getBody();
        assertThat(taskResponse).isNotNull();
        assertThat(Objects.requireNonNull(taskResponse).priority()).isEqualTo(TaskPriority.HIGH.toString());
    }

    @Test
    void testAssignTask() {
        Long taskId = 2L;
        Long userId = 3L;

        var response = testRestTemplate.exchange(
                "/api/v1/tasks/{taskId}/assignee/{userId}",
                HttpMethod.PATCH,
                new HttpEntity<>(getHeadersAdmin()),
                TaskResponse.class,
                taskId, userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var taskResponse = response.getBody();
        assertThat(taskResponse).isNotNull();
        assertThat(Objects.requireNonNull(taskResponse).assigneeId()).isEqualTo(userId);
    }

    @Test
    void testGetCommentsByTask() {
        Long taskId = 2L;

        var response = testRestTemplate.exchange(
                "/api/v1/tasks/{taskId}/comments",
                HttpMethod.GET,
                new HttpEntity<>(getHeadersAdmin()),
                TaskResponse.class,
                taskId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testAddCommentToTask() {
        Long taskId = 2L;
        var commentRequest = new CommentRequest("Комментарий для задачи");

        var response = testRestTemplate.postForEntity(
                "/api/v1/tasks/{taskId}/comments",
                new HttpEntity<>(commentRequest, getHeadersAdmin()),
                CommentResponse.class,
                taskId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var commentResponse = response.getBody();
        assertThat(commentResponse).isNotNull();
        assertThat(Objects.requireNonNull(commentResponse).comment()).isEqualTo("Комментарий для задачи");
    }
}