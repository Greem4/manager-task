package ru.greemlab.managertask.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Перечисление приоритетов задачи.
 */
@Schema(description = "Приоритеты задачи")
public enum TaskPriority {

    @Schema(description = "Высокий приоритет")
    HIGH,

    @Schema(description = "Средний приоритет")
    MEDIUM,

    @Schema(description = "Низкий приоритет")
    LOW
}
