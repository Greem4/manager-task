package ru.greemlab.managertask.integration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
        Long taskId = 2L;

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
}