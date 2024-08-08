package org.carshop.modelTest;

import org.carshop.model.*;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {

    /**
     * Тестирует создание объекта Order и проверяет правильность установки полей.
     */
    @Test
    public void testOrderConstructor() {
        // Создание объектов User и Car для тестирования
        User user = new User("1", "JohnDoe", "password123", Role.CLIENT);
        Car car = new Car("1", "Toyota", "Camry", 2022, 25000.0, "New");
        Date date = new Date();
        OrderStatus status = OrderStatus.PENDING;

        // Создание объекта Order с помощью конструктора с параметрами
        Order order = new Order("1", user, car, date, status);

        // Проверка значений полей
        assertEquals("1", order.getId(), "ID should be '1'");
        assertEquals(user, order.getClient(), "Client should be the provided user");
        assertEquals(car, order.getCar(), "Car should be the provided car");
        assertEquals(date, order.getDate(), "Date should be the provided date");
        assertEquals(status, order.getStatus(), "Status should be the provided status");
    }

    /**
     * Тестирует корректность работы методов геттеров и сеттеров.
     */
    @Test
    public void testOrderGettersAndSetters() {
        // Создание объектов User и Car для тестирования
        User user = new User("2", "JaneDoe", "password456", Role.CLIENT);
        Car car = new Car("2", "Honda", "Accord", 2023, 27000.0, "Used");
        Date date = new Date();
        OrderStatus status = OrderStatus.COMPLETED;

        // Создание объекта Order с помощью конструктора по умолчанию
        Order order = new Order();

        // Установка значений полей
        order.setId("2");
        order.setClient(user);
        order.setCar(car);
        order.setDate(date);
        order.setStatus(status);

        // Проверка значений полей
        assertEquals("2", order.getId(), "ID should be '2'");
        assertEquals(user, order.getClient(), "Client should be the provided user");
        assertEquals(car, order.getCar(), "Car should be the provided car");
        assertEquals(date, order.getDate(), "Date should be the provided date");
        assertEquals(status, order.getStatus(), "Status should be the provided status");
    }
}
