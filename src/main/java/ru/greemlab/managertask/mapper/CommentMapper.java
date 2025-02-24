package ru.greemlab.managertask.mapper;

import org.springframework.stereotype.Component;
import ru.greemlab.managertask.domain.dto.CommentResponse;
import ru.greemlab.managertask.domain.model.TaskComment;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

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

    public List<CommentResponse> toCommentResponseList(List<TaskComment> comments) {
        return comments.stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
    }
}
