package com.services.employee.repository.impl;

import com.services.employee.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
    }

    @Test
    void save_ShouldAddUser() {
        User user = new User("John Doe", "john@example.com", "password", "ROLE_USER");
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("John Doe", savedUser.getName());
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenExists() {
        User user = new User("John Doe", "john@example.com", "password", "ROLE_USER");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("john@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenNotExists() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertTrue(foundUser.isEmpty());
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenExists() {
        User user = new User("John Doe", "john@example.com", "password", "ROLE_USER");
        userRepository.save(user);

        assertTrue(userRepository.existsByEmail("john@example.com"));
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenNotExists() {
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }
}
