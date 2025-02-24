package ru.greemlab.managertask.util.security;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Утилитный класс для работы с безопасностью.
 * Предоставляет методы для извлечения информации о текущем аутентифицированном пользователе.
 */
public class SecurityUtils {

    private SecurityUtils() {}

    /**
     * Извлекает имя текущего аутентифицированного пользователя.
     *
     * @return имя текущего пользователя или {@code null}, если аутентификация не выполнена.
     */
    public static String getCurrentUserName() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? auth.getName() : null;
    }
}
