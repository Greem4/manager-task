package ru.greemlab.managertask.domain.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String comment,
        Long taskId,
        Long userId,
        String userEmail,
        LocalDateTime createdAt
) {
}
