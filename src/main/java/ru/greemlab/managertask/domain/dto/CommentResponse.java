package ru.greemlab.managertask.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO для ответа с информацией о комментарии к задаче.
 */
@Schema(description = "Ответ с информацией о комментарии к задаче")
public record CommentResponse(

        @Schema(description = "Идентификатор комментария", example = "1")
        Long id,

        @Schema(description = "Текст комментария", example = "Задача была успешно выполнена.")
        String comment,

        @Schema(description = "Идентификатор задачи", example = "10")
        Long taskId,

        @Schema(description = "Идентификатор пользователя, оставившего комментарий", example = "2")
        Long userId,

        @Schema(description = "Электронная почта пользователя", example = "user@example.com")
        String userEmail,

        @Schema(description = "Дата и время создания комментария", example = "2025-02-24T14:30:00")
        LocalDateTime createdAt
) {
}
