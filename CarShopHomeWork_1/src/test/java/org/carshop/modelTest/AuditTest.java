package org.carshop.modelTest;

import org.carshop.model.Audit;
import org.carshop.model.Role;
import org.carshop.model.User;
import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuditTest {

    /**
     * Тестирует создание объекта Audit и проверяет правильность установки полей.
     */
    /**
     * Тестирует создание объекта Audit и проверяет правильность установки полей.
     */
    @Test
    public void testAuditConstructor() {
        // Создание объекта User для тестирования
        User user = new User("1", "JohnDoe", "password123", Role.CLIENT);
        Date date = new Date();

        // Создание объекта Audit с помощью конструктора с параметрами
        Audit audit = new Audit("1", user, "User login", date);

        // Проверка значений полей
        assertEquals("1", audit.getId());
        assertEquals(user, audit.getUser());
        assertEquals("User login", audit.getAction());
        assertTrue(date.equals(audit.getDate()), "Dates should be equal");
    }

    /**
     * Тестирует корректность работы методов геттеров и сеттеров.
     */
    @Test
    public void testAuditGettersAndSetters() {
        // Создание объекта User для тестирования
        User user = new User("2", "JaneDoe", "password456", Role.CLIENT);
        Date date = new Date();

        // Создание объекта Audit с помощью конструктора по умолчанию
        Audit audit = new Audit();

        // Установка значений полей
        audit.setId("2");
        audit.setUser(user);
        audit.setAction("User logout");
        audit.setDate(date);

        // Проверка значений полей
        assertEquals("2", audit.getId());
        assertEquals(user, audit.getUser());
        assertEquals("User logout", audit.getAction());
        assertTrue(date.equals(audit.getDate()), "Dates should be equal");
    }
}
