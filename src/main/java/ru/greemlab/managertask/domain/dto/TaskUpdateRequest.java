package ru.greemlab.managertask.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ru.greemlab.managertask.domain.model.TaskPriority;
import ru.greemlab.managertask.domain.model.TaskStatus;

/**
 * DTO для запроса на обновление задачи.
 */
@Builder
@Schema(description = "Запрос на обновление существующей задачи")
public record TaskUpdateRequest(

        @Schema(description = "Название задачи", example = "Обновить функционал")
        String title,

        @Schema(description = "Описание задачи", example = "Необходимо обновить функционал для страницы пользователя.")
        String description,

        @Schema(description = "Статус задачи", allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED"}, example = "IN_PROGRESS")
        TaskStatus status,

        @Schema(description = "Приоритет задачи", allowableValues = {"HIGH", "MEDIUM", "LOW"}, example = "MEDIUM")
        TaskPriority priority,

        @Schema(description = "Идентификатор нового исполнителя задачи", example = "4")
        Long assigneeId
) {
}
