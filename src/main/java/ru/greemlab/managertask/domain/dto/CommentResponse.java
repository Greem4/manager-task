package ru.greemlab.managertask.domain.dto;

public record CommentResponse(
        Long id,
        String comment,
        Long taskId,
        Long userId,
        String userEmail,
        String createdAt
) {
}
