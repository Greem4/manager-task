package ru.greemlab.managertask.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Модель задачи.
 * Содержит информацию о задаче, такую как название, описание, статус, приоритет, автор и исполнитель.
 */
@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор задачи", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Название задачи", example = "Разработать новый функционал")
    private String title;

    @Column(nullable = false)
    @Schema(description = "Описание задачи", example = "Необходимо разработать новый функционал для страницы пользователя.")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Статус задачи", allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED"}, example = "PENDING")
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Приоритет задачи", allowableValues = {"HIGH", "MEDIUM", "LOW"}, example = "HIGH")
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @Schema(description = "Автор задачи", example = "2")
    private User author;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    @Schema(description = "Исполнитель задачи", example = "3")
    private User assignee;
}
