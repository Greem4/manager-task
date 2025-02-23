package ru.greemlab.managertask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.greemlab.managertask.domain.model.Role;
import ru.greemlab.managertask.repository.UserRepository;
import ru.greemlab.managertask.domain.model.User;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public User getById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователя не существует"));
    }

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        return save(user);
    }

    public User getByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь c такой почтой не найден"));
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }

    public User updateUserRole(String username, Role role) {
        var user = getByUsername(username);
        user.setRole(role);
        return save(user);
    }
}
