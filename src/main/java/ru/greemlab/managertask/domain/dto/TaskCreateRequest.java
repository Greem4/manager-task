package ru.greemlab.managertask.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import ru.greemlab.managertask.domain.model.TaskPriority;
import ru.greemlab.managertask.domain.model.TaskStatus;

@Builder
public record TaskCreateRequest(
        @NotBlank(message = "Назначение не может быть пустым")
        String title,

        @NotBlank(message = "Описание не может быть пустым")
        String description,

        TaskStatus status,
        TaskPriority priority,
        Long assigneeId
) {
}
