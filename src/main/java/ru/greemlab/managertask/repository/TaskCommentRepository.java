package ru.greemlab.managertask.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.greemlab.managertask.domain.model.TaskComment;

/**
 * Репозиторий для работы с сущностью TaskComment.
 * Предоставляет доступ к данным комментариев задач в базе данных.
 */
public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {

    /**
     * Находит комментарии для задачи по ее ID.
     *
     * @param taskId   ID задачи.
     * @param pageable Параметры постраничного вывода.
     * @return Страница комментариев для указанной задачи.
     */
    Page<TaskComment> findByTaskId(Long taskId, Pageable pageable);
}
