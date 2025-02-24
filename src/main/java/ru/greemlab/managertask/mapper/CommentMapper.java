package ru.greemlab.managertask.mapper;

import org.springframework.stereotype.Component;
import ru.greemlab.managertask.domain.dto.CommentResponse;
import ru.greemlab.managertask.domain.model.TaskComment;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер для преобразования объектов TaskComment в CommentResponse.
 * Позволяет конвертировать данные о комментарии в ответный формат.
 */
@Component
public class CommentMapper {

    /**
     * Преобразует объект TaskComment в CommentResponse.
     *
     * @param comment Объект TaskComment.
     * @return Преобразованный объект CommentResponse.
     */
    public CommentResponse toCommentResponse(TaskComment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                comment.getUser().getEmail(),
                comment.getCreatedAt()
        );
    }

    /**
     * Преобразует список объектов TaskComment в список CommentResponse.
     *
     * @param comments Список объектов TaskComment.
     * @return Список преобразованных объектов CommentResponse.
     */
    public List<CommentResponse> toCommentResponseList(List<TaskComment> comments) {
        return comments.stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
    }
}
