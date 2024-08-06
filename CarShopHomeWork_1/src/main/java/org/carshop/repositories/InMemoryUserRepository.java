package org.carshop.repositories;

import org.carshop.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository{
    private List<User> users = new ArrayList<>();
    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    public User findById(String id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public void update(User user) {
        Optional<User> existingUser = users.stream().filter(u -> u.getId().equals(user.getId())).findFirst();
        existingUser.ifPresent(value -> {
            value.setUsername(user.getUsername());
            value.setPassword(user.getPassword());
            value.setRole(user.getRole());
            value.setPurchaseCount(user.getPurchaseCount()); // Добавьте эту строку
        });
    }

    @Override
    public void delete(String id) {
        users.removeIf(user -> user.getId().equals(id));
    }

    @Override
    public long count() {
        return users.size();
    }
}
