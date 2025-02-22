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
VALUES ('admin@admin.ru', 'admin',
        '$2a$10$QwJZKdG5jkPyQouykNSaRuza5RCmGjNwLGWVjfb7/Ob5Atn17uZLy',
        'ROLE_ADMIN');


