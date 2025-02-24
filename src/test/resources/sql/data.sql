TRUNCATE TABLE users RESTART IDENTITY CASCADE;
INSERT INTO users (email, username, password, role)
VALUES ('admin@admin.ru', 'admin',
        '$2a$10$QwJZKdG5jkPyQouykNSaRuza5RCmGjNwLGWVjfb7/Ob5Atn17uZLy',
        'ROLE_ADMIN'),
       ('user@user.com', 'user',
        '$2a$10$c8ispWShELshlVSRVkne4eHQaFFGEoB6YBUj58/zhq9GOufwc4E9C',
        'ROLE_USER');
INSERT INTO tasks (id, title, description, status, priority, author_id, assignee_id)
VALUES (1, 'Интеграционный тест', 'Проверка создания задачи', 'PENDING', 'LOW',
        1, 2);