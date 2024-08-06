package org.carshop.services;

import org.carshop.model.Order;
import org.carshop.model.OrderStatus;
import org.carshop.model.Role;
import org.carshop.model.User;
import org.carshop.repositories.OrderRepository;
import org.carshop.repositories.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private User currentUser; // Поле для хранения текущего пользователя

    public UserService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public boolean registerUser(String username, String password, Role role) {
        if (userRepository.findByUsername(username) == null) {
            String id = generateId();
            User user = new User(id, username, password, role);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user; // Устанавливаем текущего пользователя при успешном логине
            return user;
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private String generateId() {
        return String.valueOf(userRepository.count() + 1);
    }

    public void delete(String id) {
        userRepository.delete(id);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Новый метод update
    public void update(User user) {
        userRepository.update(user);
    }

    public List<User> getUsersByName(String name) {
        return userRepository.findAll().stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());
    }
    public User findById(String Id) {
        return userRepository.findById(Id);
    }

    public List<User> sortUsersByPurchases(boolean ascending) {
        return userRepository.findAll().stream()
                .sorted(ascending
                        ? Comparator.comparingInt(User::getPurchaseCount)
                        : Comparator.comparingInt(User::getPurchaseCount).reversed())
                .collect(Collectors.toList());
    }
    // Новый метод для обновления информации всех учетных записей
    public void updateAllUsers(User updatedUser) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            // Обновление каждого пользователя на основе информации из updatedUser
            if (user.getId().equals(updatedUser.getId())) {
                user.setUsername(updatedUser.getUsername());
                user.setPassword(updatedUser.getPassword());
                user.setRole(updatedUser.getRole());
                userRepository.update(user);
            }
        }
    }
}
