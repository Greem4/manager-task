package ru.greemlab.managertask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.greemlab.managertask.domain.model.TaskComment;

import java.util.List;

public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
    List<TaskComment> findByTaskId(Long taskId);
}
