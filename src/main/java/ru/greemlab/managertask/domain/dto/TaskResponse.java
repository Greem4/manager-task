package ru.greemlab.managertask.domain.dto;

import lombok.Builder;

@Builder
public record TaskResponse(
        Long id,
        String title,
        String description,
        String status,
        String priority,
        Long authorId,
        Long assigneeId
) {
}
