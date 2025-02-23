package ru.greemlab.managertask.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.greemlab.managertask.domain.model.Task;
import ru.greemlab.managertask.domain.model.User;


public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByAuthor(User author, Pageable pageable);

    Page<Task> findByAssignee(User assignee, Pageable pageable);
}
