package org.carshop.modelTest;

import org.carshop.model.Car;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarTest {

    /**
     * Тестирует создание объекта Car и проверяет правильность установки полей.
     */
    @Test
    public void testCarConstructor() {
        // Создание объекта Car
        Car car = new Car("1", "Toyota", "Corolla", 2020, 20000.0, "New");

        // Проверка значений полей
        assertEquals("1", car.getId(), "ID should be '1'");
        assertEquals("Toyota", car.getMake(), "Make should be 'Toyota'");
        assertEquals("Corolla", car.getModel(), "Model should be 'Corolla'");
        assertEquals(2020, car.getYear(), "Year should be 2020");
        assertEquals(20000.0, car.getPrice(), "Price should be 20000.0");
        assertEquals("New", car.getCondition(), "Condition should be 'New'");
    }

    /**
     * Тестирует корректность работы методов геттеров и сеттеров.
     */
    @Test
    public void testCarGettersAndSetters() {
        // Создание объекта Car
        Car car = new Car();

        // Установка значений полей
        car.setId("2");
        car.setMake("Honda");
        car.setModel("Civic");
        car.setYear(2021);
        car.setPrice(22000.0);
        car.setCondition("Used");

        // Проверка значений полей
        assertEquals("2", car.getId(), "ID should be '2'");
        assertEquals("Honda", car.getMake(), "Make should be 'Honda'");
        assertEquals("Civic", car.getModel(), "Model should be 'Civic'");
        assertEquals(2021, car.getYear(), "Year should be 2021");
        assertEquals(22000.0, car.getPrice(), "Price should be 22000.0");
        assertEquals("Used", car.getCondition(), "Condition should be 'Used'");
    }
}