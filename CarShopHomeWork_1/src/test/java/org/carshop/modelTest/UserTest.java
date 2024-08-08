package org.carshop.modelTest;

import org.carshop.model.Role;
import org.carshop.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {

    @Test
    public void testUserConstructorAndInitialValues() {
        // Создаем объект User
        String id = "1";
        String username = "testuser";
        String password = "password";
        Role role = Role.CLIENT;

        // Создаем экземпляр User
        User user = new User(id, username, password, role);

        // Проверяем, что объект создан и значения установлены правильно
        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(role, user.getRole());
        assertEquals(0, user.getPurchaseCount()); // Проверка начального значения purchaseCount
    }

    @Test
    public void testSetters() {
        // Создаем объект User
        User user = new User("1", "testuser", "password", Role.CLIENT);

        // Изменяем значения через сеттеры
        user.setId("2");
        user.setUsername("newuser");
        user.setPassword("newpassword");
        user.setRole(Role.MANAGER);
        user.setPurchaseCount(5);

        // Проверяем, что значения установлены правильно
        assertEquals("2", user.getId());
        assertEquals("newuser", user.getUsername());
        assertEquals("newpassword", user.getPassword());
        assertEquals(Role.MANAGER, user.getRole());
        assertEquals(5, user.getPurchaseCount());
    }

    @Test
    public void testRoleEnum() {
        // Проверяем, что роли правильно отображаются
        assertEquals("Клиент", Role.getDisplayName(Role.CLIENT));
        assertEquals("Менеджер", Role.getDisplayName(Role.MANAGER));
        assertEquals("Администратор", Role.getDisplayName(Role.ADMIN));
    }

    @Test
    public void testInvalidRole() {
        // Проверяем, что при передаче несуществующей роли возвращается название роли
        assertEquals("UNKNOWN_ROLE", Role.getDisplayName(null));
    }
}
