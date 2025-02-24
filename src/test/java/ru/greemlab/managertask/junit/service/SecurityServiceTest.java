package ru.greemlab.managertask.junit.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.greemlab.managertask.service.SecurityService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        securityService = new SecurityService();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUserName_WhenUserAuthenticated_ReturnsUsername() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser@test.com");
        SecurityContextHolder.getContext().setAuthentication(auth);

        var result = securityService.getCurrentUserName();

        assertEquals("testuser@test.com", result);
    }

    @Test
    void getCurrentUserName_WhenNoAuthentication_ReturnsNull() {
        SecurityContextHolder.clearContext();

        var result = securityService.getCurrentUserName();

        assertNull(result);
    }
}