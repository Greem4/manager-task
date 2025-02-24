package ru.greemlab.managertask.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * DTO для ответа с информацией о задаче.
 */
@Builder
@Schema(description = "Ответ с информацией о задаче")
public record TaskResponse(

        @Schema(description = "Идентификатор задачи", example = "1")
        Long id,

        @Schema(description = "Название задачи", example = "Разработать новый функционал")
        String title,

        @Schema(description = "Описание задачи", example = "Необходимо разработать новый функционал для страницы пользователя.")
        String description,

        @Schema(description = "Статус задачи", allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED"}, example = "OPEN")
        String status,

        @Schema(description = "Приоритет задачи", allowableValues = {"LOW", "MEDIUM", "HIGH"}, example = "HIGH")
        String priority,

        @Schema(description = "Идентификатор автора задачи", example = "2")
        Long authorId,

        @Schema(description = "Идентификатор исполнителя задачи", example = "3")
        Long assigneeId
) {
}
