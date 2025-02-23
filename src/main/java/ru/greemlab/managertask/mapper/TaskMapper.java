package ru.greemlab.managertask.mapper;

import org.springframework.stereotype.Component;
import ru.greemlab.managertask.domain.dto.TaskCreateRequest;
import ru.greemlab.managertask.domain.dto.TaskResponse;
import ru.greemlab.managertask.domain.dto.TaskUpdateRequest;
import ru.greemlab.managertask.domain.model.Task;
import ru.greemlab.managertask.domain.model.User;

@Component
public class TaskMapper {

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
