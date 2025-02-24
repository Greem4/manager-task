TRUNCATE TABLE users RESTART IDENTITY CASCADE;
INSERT INTO users (email, username, password, role)
VALUES ('admin@mail.ru', 'admin',
        '$2a$10$gBY2ViYvLULQSiIdn0E85.bbzDn2z0hAk0S4hicMFikzNWaOEV.86',
        'ROLE_ADMIN'),
       ('user@mail.ru', 'user',
        '$2a$10$OcKeJhVizXLJ2w1VDVf2YeXpyVTXN6aFAL04s3UxMHU3HpFMHisvq',
        'ROLE_USER'),
       ('user2@mail.ru', 'user2',
        '$2a$10$HG1gjtPnJJYJZSFHqahn6uGzwmSw8Q/yeaBpOu8JGYWWJ77FbGapO',
        'ROLE_USER');
INSERT INTO tasks (id, title, description, status, priority, author_id,
                   assignee_id)
VALUES (2, 'Интеграционный тест', 'Проверка создания задачи', 'PENDING', 'LOW',
        1, 2),
       (3, 'Интеграционный тест', 'Проверка создания задачи', 'PENDING', 'LOW',
        2, 2);