package ru.greemlab.managertask.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import ru.greemlab.managertask.domain.model.TaskPriority;
import ru.greemlab.managertask.domain.model.TaskStatus;

/**
 * DTO для запроса на создание новой задачи.
 */
@Builder
@Schema(description = "Запрос на создание новой задачи")
public record TaskCreateRequest(

        @NotBlank(message = "Назначение не может быть пустым")
        @Schema(description = "Название задачи", example = "Разработать новый функционал")
        String title,

        @NotBlank(message = "Описание не может быть пустым")
        @Schema(description = "Описание задачи", example = "Необходимо разработать новый функционал для страницы пользователя.")
        String description,

        @Schema(description = "Статус задачи", allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED"}, example = "PENDING")
        TaskStatus status,

        @Schema(description = "Приоритет задачи", allowableValues = {"LOW", "MEDIUM", "HIGH"}, example = "HIGH")
        TaskPriority priority,

        @Schema(description = "Идентификатор исполнителя задачи", example = "2")
        Long assigneeId
) {
}
