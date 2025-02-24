--liquibase formatted sql

--changeset greemlab:1
CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    role     VARCHAR(50)         NOT NULL
);

--changeset greemlab:2
INSERT INTO users (email, username, password, role)
VALUES ('admin@mail.ru', 'admin',
        '$2a$10$gBY2ViYvLULQSiIdn0E85.bbzDn2z0hAk0S4hicMFikzNWaOEV.86',
        'ROLE_ADMIN'),
       ('user@mail.ru', 'user',
        '$2a$10$OcKeJhVizXLJ2w1VDVf2YeXpyVTXN6aFAL04s3UxMHU3HpFMHisvq',
        'ROLE_USER');

--changeset greemlab:3
CREATE TABLE tasks
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    status      VARCHAR(50)  NOT NULL,
    priority    VARCHAR(50)  NOT NULL,
    author_id   BIGINT       NOT NULL,
    assignee_id BIGINT       NULL,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_assignee FOREIGN KEY (assignee_id) REFERENCES users (id) ON DELETE SET NULL
);

--changeset greemlab:4
CREATE TABLE task_comments
(
    id         BIGSERIAL PRIMARY KEY,
    task_id    BIGINT                              NOT NULL,
    user_id    BIGINT                              NOT NULL,
    comment    TEXT                                NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
