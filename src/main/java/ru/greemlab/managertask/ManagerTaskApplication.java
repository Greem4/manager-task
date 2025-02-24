package ru.greemlab.managertask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Основной класс для запуска Spring Boot приложения.
 * Этот класс используется для запуска приложения, конфигурируя его с помощью аннотации {@link SpringBootApplication}.
 */
@SpringBootApplication
public class ManagerTaskApplication {

    /**
     * Главный метод для запуска приложения.
     *
     * @param args Массив аргументов командной строки.
     */
    public static void main(String[] args) {
        SpringApplication.run(ManagerTaskApplication.class, args);
    }
}
