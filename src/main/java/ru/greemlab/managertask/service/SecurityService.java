package ru.greemlab.managertask.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с безопасностью.
 * Предоставляет методы для извлечения информации о текущем аутентифицированном пользователе.
 */
@Service
public class SecurityService {

    /**
     * Извлекает имя текущего аутентифицированного пользователя.
     *
     * @return имя текущего пользователя или {@code null}, если аутентификация не выполнена.
     */
    public String getCurrentUserName() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? auth.getName() : null;
    }

}
