package ru.greemlab.managertask.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.greemlab.managertask.domain.model.Task;
import ru.greemlab.managertask.domain.model.User;

/**
 * Репозиторий для работы с сущностью Task.
 * Предоставляет доступ к данным задач в базе данных.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Находит задачи, созданные указанным автором.
     *
     * @param author   Автор задач.
     * @param pageable Параметры постраничного вывода.
     * @return Страница задач, созданных автором.
     */
    Page<Task> findByAuthor(User author, Pageable pageable);

    /**
     * Находит задачи, назначенные указанному исполнителю.
     *
     * @param assignee Исполнитель задач.
     * @param pageable Параметры постраничного вывода.
     * @return Страница задач, назначенных исполнителю.
     */
    Page<Task> findByAssignee(User assignee, Pageable pageable);
}
