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

/**
 * Сервис для управления пользователями.
 * <p>Этот сервис предоставляет методы для регистрации, логина, обновления, удаления и получения пользователей.
 * Также включает функции для фильтрации пользователей по имени и роли, а также сортировки по числу покупок.</p>
 */
public class UserService {
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private User currentUser; // Поле для хранения текущего пользователя

    /**
     * Конструктор класса {@code UserService}.
     *
     * @param userRepository репозиторий для управления пользователями
     * @param orderRepository репозиторий для управления заказами
     */
    public UserService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @param role роль пользователя
     * @return {@code true}, если регистрация прошла успешно, {@code false} если имя пользователя уже занято
     */
    public boolean registerUser(String username, String password, Role role) {
        if (userRepository.findByUsername(username) == null) {
            String id = generateId();
            User user = new User(id, username, password, role);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * Выполняет вход пользователя в систему.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return пользователь, если вход выполнен успешно, {@code null} в противном случае
     */
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user; // Устанавливаем текущего пользователя при успешном логине
            return user;
        }
        return null;
    }

    /**
     * Возвращает список всех пользователей.
     *
     * @return список всех пользователей
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Возвращает пользователя по имени.
     *
     * @param username имя пользователя
     * @return пользователь с указанным именем, или {@code null}, если пользователь не найден
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Генерирует уникальный идентификатор для нового пользователя.
     *
     * @return уникальный идентификатор
     */
    private String generateId() {
        return String.valueOf(userRepository.count() + 1);
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     */
    public void delete(String id) {
        userRepository.delete(id);
    }

    /**
     * Возвращает текущего пользователя.
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Устанавливает текущего пользователя.
     *
     * @param user пользователь для установки как текущий
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Обновляет информацию о пользователе.
     *
     * @param user пользователь с обновленной информацией
     */
    public void update(User user) {
        userRepository.update(user);
    }

    /**
     * Возвращает список пользователей с указанным именем.
     *
     * @param name имя пользователя
     * @return список пользователей с указанным именем
     */
    public List<User> getUsersByName(String name) {
        return userRepository.findAll().stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список пользователей с указанной ролью.
     *
     * @param role роль пользователя
     * @return список пользователей с указанной ролью
     */
    public List<User> getUsersByRole(Role role) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param Id идентификатор пользователя
     * @return пользователь с указанным идентификатором, или {@code null}, если пользователь не найден
     */
    public User findById(String Id) {
        return userRepository.findById(Id);
    }

    /**
     * Возвращает список пользователей, отсортированный по количеству покупок.
     *
     * @param ascending {@code true}, если сортировка по возрастанию, {@code false}, если по убыванию
     * @return отсортированный список пользователей
     */
    public List<User> sortUsersByPurchases(boolean ascending) {
        return userRepository.findAll().stream()
                .sorted(ascending
                        ? Comparator.comparingInt(User::getPurchaseCount)
                        : Comparator.comparingInt(User::getPurchaseCount).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Обновляет информацию всех пользователей на основе информации из переданного объекта {@code updatedUser}.
     *
     * @param updatedUser объект с обновленной информацией для пользователей
     */
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

    /**
     * Обновляет индексы пользователей, чтобы они начинались с 1 и увеличивались на 1 для каждого следующего пользователя.
     */
    public void updateUserIndices() {
        List<User> users = userRepository.findAll();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            user.setId(String.valueOf(i + 1));
            userRepository.update(user);
        }
    }
}
