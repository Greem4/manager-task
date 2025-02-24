package ru.greemlab.managertask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.greemlab.managertask.domain.model.User;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью User.
 * Предоставляет доступ к данным пользователей в базе данных.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по адресу электронной почты.
     *
     * @param email Адрес электронной почты пользователя.
     * @return Optional с найденным пользователем.
     */
    Optional<User> findByEmail(String email);

    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username Имя пользователя.
     * @return Optional с найденным пользователем.
     */
    Optional<User> findByUsername(String username);

    /**
     * Проверяет существование пользователя с указанным именем.
     *
     * @param username Имя пользователя.
     * @return true, если пользователь с таким именем существует, иначе false.
     */
    boolean existsByUsername(String username);

    /**
     * Проверяет существование пользователя с указанным адресом электронной почты.
     *
     * @param email Адрес электронной почты.
     * @return true, если пользователь с таким email существует, иначе false.
     */
    boolean existsByEmail(String email);
}
