package ru.greemlab.managertask.domain.dto;

import lombok.Builder;
import ru.greemlab.managertask.domain.model.TaskPriority;
import ru.greemlab.managertask.domain.model.TaskStatus;

@Builder
public record TaskUpdateRequest(
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        Long assigneeId
) {
}
