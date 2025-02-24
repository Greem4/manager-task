package ru.greemlab.managertask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.greemlab.managertask.domain.model.Role;
import ru.greemlab.managertask.repository.UserRepository;
import ru.greemlab.managertask.domain.model.User;

/**
 * Сервис для работы с пользователями.
 * <p>
 * Этот сервис предоставляет методы для создания, получения и обновления данных пользователя.
 * Он также использует репозиторий для взаимодействия с базой данных.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    /**
     * Сохранение пользователя в базе данных.
     *
     * @param user Пользователь, которого нужно сохранить.
     * @return Сохраненный пользователь.
     */
    public User save(User user) {
        return repository.save(user);
    }

    /**
     * Получение пользователя по ID.
     *
     * @param userId ID пользователя.
     * @return Пользователь.
     * @throws UsernameNotFoundException если пользователь не найден.
     */
    public User getById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователя не существует"));
    }

    /**
     * Создание нового пользователя.
     *
     * @param user Данные для создания пользователя.
     * @return Созданный пользователь.
     * @throws RuntimeException если пользователь с таким именем или email уже существует.
     */
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        return save(user);
    }

    /**
     * Получение пользователя по email.
     *
     * @param email Email пользователя.
     * @return Пользователь.
     * @throws UsernameNotFoundException если пользователь с таким email не найден.
     */
    public User getByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь c такой почтой не найден"));
    }

    /**
     * Получение пользователя по username.
     *
     * @param username Имя пользователя.
     * @return Пользователь.
     * @throws UsernameNotFoundException если пользователь с таким именем не найден.
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    /**
     * Получение службы UserDetailsService для аутентификации пользователя.
     *
     * @return UserDetailsService, использующий email для поиска пользователя.
     */
    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }

    /**
     * Обновление роли пользователя.
     *
     * @param username Имя пользователя.
     * @param role Новая роль пользователя.
     * @return Обновленный пользователь.
     */
    public User updateUserRole(String username, Role role) {
        var user = getByUsername(username);
        user.setRole(role);
        return save(user);
    }
}
