package ru.greemlab.managertask.mapper;

import org.springframework.stereotype.Component;
import ru.greemlab.managertask.domain.dto.TaskCreateRequest;
import ru.greemlab.managertask.domain.dto.TaskResponse;
import ru.greemlab.managertask.domain.dto.TaskUpdateRequest;
import ru.greemlab.managertask.domain.model.Task;
import ru.greemlab.managertask.domain.model.User;

/**
 * Маппер для преобразования объектов TaskCreateRequest, TaskUpdateRequest, Task в TaskResponse и обратно.
 * Позволяет преобразовывать данные из DTO в сущности и наоборот.
 */
@Component
public class TaskMapper {

    /**
     * Преобразует объект TaskCreateRequest в сущность Task.
     *
     * @param dto       Объект TaskCreateRequest.
     * @param author    Автор задачи.
     * @param assignee  Исполнитель задачи.
     * @return Преобразованная сущность Task.
     */
    public Task toEntity(TaskCreateRequest dto, User author, User assignee) {
        return Task.builder()
                .title(dto.title())
                .description(dto.description())
                .status(dto.status())
                .priority(dto.priority())
                .author(author)
                .assignee(assignee)
                .build();
    }

    /**
     * Преобразует объект TaskUpdateRequest в существующую сущность Task.
     *
     * @param dto          Объект TaskUpdateRequest.
     * @param existingTask Сущность Task, которую нужно обновить.
     * @param assignee     Новый исполнитель задачи (может быть null).
     * @return Обновленная сущность Task.
     */
    public Task toEntityForUpdate(TaskUpdateRequest dto, Task existingTask, User assignee) {
        if (dto.title() != null) {
            existingTask.setTitle(dto.title());
        }
        if (dto.description() != null) {
            existingTask.setDescription(dto.description());
        }
        if (dto.status() != null) {
            existingTask.setStatus(dto.status());
        }
        if (dto.priority() != null) {
            existingTask.setPriority(dto.priority());
        }
        if (assignee != null) {
            existingTask.setAssignee(assignee);
        }
        return existingTask;
    }

    /**
     * Преобразует сущность Task в объект TaskResponse.
     *
     * @param task Сущность Task.
     * @return Преобразованный объект TaskResponse.
     */
    public TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .priority(task.getPriority().name())
                .authorId(task.getAuthor().getId())
                .assigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null)
                .build();
    }
}
