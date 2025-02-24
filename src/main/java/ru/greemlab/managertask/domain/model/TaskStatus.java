package ru.greemlab.managertask.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Перечисление статусов задачи.
 */
@Schema(description = "Статусы задачи")
public enum TaskStatus {

    @Schema(description = "Задача ожидает выполнения")
    PENDING,

    @Schema(description = "Задача в процессе выполнения")
    IN_PROGRESS,

    @Schema(description = "Задача завершена")
    COMPLETED
}
