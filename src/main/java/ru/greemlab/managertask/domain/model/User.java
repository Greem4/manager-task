package ru.greemlab.managertask.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Модель пользователя.
 * Содержит информацию о пользователе, такую как имя, пароль, email и роль.
 * Реализует интерфейс UserDetails для использования с Spring Security.
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    @Schema(description = "Имя пользователя", example = "user")
    private String username;

    @Column(name = "password", nullable = false)
    @Schema(description = "Пароль пользователя", example = "password123")
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    @Schema(description = "Адрес электронной почты", example = "user@example.com")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Schema(description = "Роль пользователя", allowableValues = {"ROLE_USER", "ROLE_ADMIN"}, example = "ROLE_USER")
    private Role role;

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
