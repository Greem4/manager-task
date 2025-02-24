package ru.greemlab.managertask.junit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.greemlab.managertask.domain.model.Role;
import ru.greemlab.managertask.domain.model.User;
import ru.greemlab.managertask.repository.UserRepository;
import ru.greemlab.managertask.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@test.ru");
        user.setRole(Role.ROLE_USER);
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(user)).thenReturn(user);

        var savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetById_UserFound() {
        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));

        var foundUser = userService.getById(user.getId());

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void testGetById_UserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getById(user.getId()));
    }

    @Test
    void testCreate_UserAlreadyExists() {
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(user));
    }

    @Test
    void testCreate_UserEmailAlreadyExists() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(user));
    }

    @Test
    void testGetByEmail_UserFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(java.util.Optional.of(user));

        var foundUser = userService.getByEmail(user.getEmail());

        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testGetByEmail_UserNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(java.util.Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getByEmail(user.getEmail()));
    }

    @Test
    void testGetByUsername_UserFound() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.of(user));

        var foundUser = userService.getByUsername(user.getUsername());

        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void testGetByUsername_UserNotFound() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getByUsername(user.getUsername()));
    }

    @Test
    void testUpdateUserRole() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        var newRole = Role.ROLE_ADMIN;
        var updatedUser = userService.updateUserRole(user.getUsername(), newRole);

        assertNotNull(updatedUser);
        assertEquals(newRole, updatedUser.getRole());
        verify(userRepository, times(1)).save(updatedUser);
    }
}