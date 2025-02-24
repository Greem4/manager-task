package ru.greemlab.managertask.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Модель комментария к задаче.
 * Содержит информацию о комментарии, связанном с задачей, авторе комментария и времени создания.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_comments")
public class TaskComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор комментария", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @Schema(description = "Задача, к которой привязан комментарий", example = "2")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "Пользователь, который оставил комментарий", example = "3")
    private User user;

    @Column(nullable = false)
    @Schema(description = "Текст комментария", example = "Задача в процессе выполнения")
    private String comment;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Дата и время создания комментария", example = "2025-02-24T15:30:00")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
