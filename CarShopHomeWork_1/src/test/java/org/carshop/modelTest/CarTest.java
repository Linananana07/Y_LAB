package org.carshop.modelTest;

import org.carshop.model.Car;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarTest {

    @Test
    public void testCarConstructorAndGetters() {
        Car car = new Car("1", "Toyota", "Corolla", 2022, 20000.0, "New");

        assertEquals("1", car.getId());
        assertEquals("Toyota", car.getMake());
        assertEquals("Corolla", car.getModel());
        assertEquals(2022, car.getYear());
        assertEquals(20000.0, car.getPrice());
        assertEquals("New", car.getCondition());
    }

    @Test
    public void testSetters() {
        Car car = new Car("1", "Toyota", "Corolla", 2022, 20000.0, "New");

        car.setId("2");
        car.setMake("Honda");
        car.setModel("Civic");
        car.setYear(2023);
        car.setPrice(22000.0);
        car.setCondition("Used");

        assertEquals("2", car.getId());
        assertEquals("Honda", car.getMake());
        assertEquals("Civic", car.getModel());
        assertEquals(2023, car.getYear());
        assertEquals(22000.0, car.getPrice());
        assertEquals("Used", car.getCondition());
    }
}