package ru.greemlab.managertask.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO для комментария к задаче.
 */
@Schema(description = "Данные для добавления комментария к задаче")
public record CommentRequest(

        @Schema(description = "Текст комментария", example = "Необходимо уточнить требования к задаче.")
        String comment
) {
}
