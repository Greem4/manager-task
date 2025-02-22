TRUNCATE TABLE users RESTART IDENTITY CASCADE;
INSERT INTO users (email, username, password, role)
VALUES ('test@test.ru', 'test',
        '$2a$10$XJby4Vyjjz.hYdWlV.sD0O6D/cRiwhHUh4U3Kie/ZOEd6w1axc2qu',
        'ROLE_ADMIN'),
       ('test2@test2.com', 'test2',
        '$2a$10$BoucSUPvmabswnU5OqpRWewGsgE5kAFuMNTkGE0F01PLPBOwuwm6O',
        'ROLE_USER');