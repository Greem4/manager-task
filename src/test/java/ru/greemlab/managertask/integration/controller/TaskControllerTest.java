package ru.greemlab.managertask.integration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.greemlab.managertask.domain.dto.TaskCreateRequest;
import ru.greemlab.managertask.domain.dto.TaskResponse;
import ru.greemlab.managertask.domain.model.TaskPriority;
import ru.greemlab.managertask.domain.model.TaskStatus;
import ru.greemlab.managertask.integration.config.IntegrationTestBase;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TaskControllerTest extends IntegrationTestBase {

    @Test
    void testCreateTask() {
        TaskCreateRequest taskRequest = new TaskCreateRequest(
                "Интеграционный тест",
                "Проверка создания задачи",
                TaskStatus.PENDING,
                TaskPriority.LOW,
                1L);

        ResponseEntity<TaskResponse> response = testRestTemplate.postForEntity(
                "/api/v1/tasks",
                new HttpEntity<>(taskRequest, getHeadersAdmin()),
                TaskResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        TaskResponse taskResponse = response.getBody();
        assertThat(taskResponse).isNotNull();
        assertThat(Objects.requireNonNull(taskResponse).title()).isEqualTo("Интеграционный тест");
    }
}