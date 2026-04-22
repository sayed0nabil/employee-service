package com.services.employee.repository.impl;

import com.services.employee.model.User;
import com.services.employee.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    @Override
    public User save(User user) {
        users.add(user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
