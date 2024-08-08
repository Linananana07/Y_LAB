package org.carshop.repositories;

import org.carshop.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для хранения пользователей в памяти.
 */
public class InMemoryUserRepository implements UserRepository {

    private List<User> users = new ArrayList<>();

    /**
     * Сохраняет пользователя в репозитории.
     *
     * @param user пользователь для сохранения
     */
    @Override
    public void save(User user) {
        users.add(user);
    }

    /**
     * Находит пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return пользователь с указанным идентификатором, или null, если пользователь не найден
     */
    @Override
    public User findById(String id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Находит пользователя по его имени пользователя.
     *
     * @param username имя пользователя
     * @return пользователь с указанным именем, или null, если пользователь не найден
     */
    @Override
    public User findByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }

    /**
     * Возвращает список всех пользователей в репозитории.
     *
     * @return список всех пользователей
     */
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    /**
     * Обновляет информацию о пользователе в репозитории.
     *
     * @param user пользователь с обновленными данными
     */
    @Override
    public void update(User user) {
        Optional<User> existingUser = users.stream().filter(u -> u.getId().equals(user.getId())).findFirst();
        existingUser.ifPresent(value -> {
            value.setUsername(user.getUsername());
            value.setPassword(user.getPassword());
            value.setRole(user.getRole());
            value.setPurchaseCount(user.getPurchaseCount()); // Обновляем количество покупок
        });
    }

    /**
     * Удаляет пользователя из репозитория по его идентификатору.
     *
     * @param id идентификатор пользователя для удаления
     */
    @Override
    public void delete(String id) {
        users.removeIf(user -> user.getId().equals(id));
    }

    /**
     * Возвращает количество пользователей в репозитории.
     *
     * @return количество пользователей
     */
    @Override
    public long count() {
        return users.size();
    }
}
